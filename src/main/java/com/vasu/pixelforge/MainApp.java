package com.vasu.pixelforge;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainApp extends Application {

    @FXML
    private Label statusLabel;

    @FXML
    private TextArea asciiTextArea;

    @FXML
    private Slider resolutionSlider;

    private AsciiEngine asciiEngine = new AsciiEngine();
    private Stage primaryStage;
    private File currentSelectedFile;

    @FXML
    public void initialize() {
        resolutionSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (currentSelectedFile != null) {
                processImage(currentSelectedFile);
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
        loader.setController(this);

        Parent root = loader.load();
        Scene scene = new Scene(root, 1000, 800); // Increased size for better view
        primaryStage.setTitle("PixelForge Studio");
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
            processImage(selectedFile);
        }
    }

    private void processImage(File file) {
        statusLabel.setText("Processing: " + file.getName());
        try {
            int width = (int) resolutionSlider.getValue();
            String asciiArt = asciiEngine.convert(file, width);
            asciiTextArea.setText(asciiArt);
            statusLabel.setText("Done");
        } catch (IOException e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDownload() {
        if (asciiTextArea.getText().isEmpty()) {
            statusLabel.setText("Nothing to download!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save ASCII Art as Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        fileChooser.setInitialFileName("pixel_forge_art.png");

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                // Capture the TextArea content as an image
                // To ensure the whole text is captured, we might need to adjust the TextArea
                // settings
                // snapshot() usually only captures the visible area.
                // However, for ASCII art, a snapshot of the node is the most direct way in
                // JavaFX.
                WritableImage snapshot = asciiTextArea.snapshot(new SnapshotParameters(), null);

                // Convert to BufferedImage and Save
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
                statusLabel.setText("Saved as PNG: " + file.getName());
            } catch (IOException e) {
                statusLabel.setText("Error saving PNG: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
