package com.vasu.pixelforge;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MainApp extends Application {

    @FXML
    private Label statusLabel;

    @FXML
    private Canvas asciiCanvas;

    @FXML
    private CheckBox colorModeCheckBox;

    @FXML
    private Slider resolutionSlider;

    @FXML
    private VBox loadingOverlay;

    @FXML
    private javafx.scene.control.ScrollPane scrollPane;

    private AsciiEngine asciiEngine = new AsciiEngine();
    private Stage primaryStage;
    private File currentSelectedFile;
    private AsciiPixel[][] currentPixels;
    private Timeline debounceTimeline;
    private double originalImageWidth;
    private double originalImageHeight;

    @FXML
    public void initialize() {
        debounceTimeline = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            if (currentSelectedFile != null) {
                processImage(currentSelectedFile);
            }
        }));
        debounceTimeline.setCycleCount(1);

        resolutionSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            debounceTimeline.stop();
            debounceTimeline.playFromStart();
        });

        colorModeCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (currentPixels != null) {
                renderCanvas(currentPixels, false);
            }
        });

        // Add resize listeners to update the UI view
        scrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (currentPixels != null) renderCanvas(currentPixels, false);
        });
        scrollPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (currentPixels != null) renderCanvas(currentPixels, false);
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
        loader.setController(this);

        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 900); // Larger default size
        primaryStage.setTitle("PixelForge Studio - Pro v2.0");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    private void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            this.currentSelectedFile = selectedFile;
            try {
                // Pre-load image to get original dimensions
                javafx.scene.image.Image img = new javafx.scene.image.Image(selectedFile.toURI().toString());
                this.originalImageWidth = img.getWidth();
                this.originalImageHeight = img.getHeight();
                processImage(selectedFile);
            } catch (Exception e) {
                statusLabel.setText("Failed to load image metadata");
            }
        }
    }

    private void processImage(File file) {
        statusLabel.setText("Processing: " + file.getName());
        loadingOverlay.setVisible(true);

        Task<AsciiPixel[][]> task = new Task<>() {
            @Override
            protected AsciiPixel[][] call() throws Exception {
                int width = (int) resolutionSlider.getValue();
                return asciiEngine.convertToPixels(file, width);
            }
        };

        task.setOnSucceeded(e -> {
            this.currentPixels = task.getValue();
            renderCanvas(currentPixels, false);
            loadingOverlay.setVisible(false);
            statusLabel.setText("System Idle");
        });

        task.setOnFailed(e -> {
            loadingOverlay.setVisible(false);
            statusLabel.setText("Error: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    /**
     * Renders the ASCII art to the canvas.
     * @param pixels The ASCII pixels to render.
     * @param forExport If true, it renders at original resolution. If false, it fits the window.
     */
    private void renderCanvas(AsciiPixel[][] pixels, boolean forExport) {
        if (pixels == null || pixels.length == 0) return;

        int rows = pixels.length;
        int cols = pixels[0].length;

        double targetWidth, targetHeight;

        if (forExport) {
            targetWidth = originalImageWidth;
            targetHeight = originalImageHeight;
        } else {
            // Adaptive View: Subtract small margin to avoid scrollbars
            targetWidth = scrollPane.getViewportBounds().getWidth() - 5;
            targetHeight = scrollPane.getViewportBounds().getHeight() - 5;
            
            // Maintain aspect ratio of original image
            double imgAspect = originalImageWidth / originalImageHeight;
            double targetAspect = targetWidth / targetHeight;
            
            if (targetAspect > imgAspect) {
                targetWidth = targetHeight * imgAspect;
            } else {
                targetHeight = targetWidth / imgAspect;
            }
        }

        Canvas renderingTarget;
        if (forExport) {
            renderingTarget = new Canvas(targetWidth, targetHeight);
        } else {
            renderingTarget = asciiCanvas;
            renderingTarget.setWidth(targetWidth);
            renderingTarget.setHeight(targetHeight);
        }

        double charWidth = targetWidth / cols;
        double charHeight = targetHeight / rows;

        GraphicsContext gc = renderingTarget.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, targetWidth, targetHeight);

        // Scale font to fit perfectly
        gc.setFont(Font.font("Monospaced", charHeight)); 
        
        boolean colorMode = colorModeCheckBox.isSelected();

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                AsciiPixel pixel = pixels[y][x];
                if (colorMode) {
                    gc.setFill(pixel.getColor());
                } else {
                    gc.setFill(Color.web("#00ff00"));
                }
                gc.fillText(String.valueOf(pixel.getCharacter()), x * charWidth, (y + 1) * charHeight - 2);
            }
        }
        
        if (forExport) {
            // This case handles the download snapshotting logic
            this.lastExportedCanvas = renderingTarget;
        }
    }

    private Canvas lastExportedCanvas;

    @FXML
    private void handleDownload() {
        if (currentPixels == null) {
            statusLabel.setText("Nothing to download!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Full-Res ASCII Art");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        fileChooser.setInitialFileName("pixel_forge_pro_export.png");

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                statusLabel.setText("Rendering high-res export...");
                renderCanvas(currentPixels, true); // Renders to lastExportedCanvas
                
                SnapshotParameters params = new SnapshotParameters();
                params.setFill(Color.BLACK);
                WritableImage snapshot = lastExportedCanvas.snapshot(params, null);
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
                statusLabel.setText("Exported (Full Res): " + file.getName());
            } catch (IOException e) {
                statusLabel.setText("Export Failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
