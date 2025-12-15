package com.vasu.pixelforge;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainApp extends Application {

    @FXML
    private Label statusLabel;

    @FXML
    private TextArea asciiTextArea;

    @FXML
    private Slider resolutionSlider;

    private AsciiEngine asciiEngine = new AsciiEngine();
    private Stage primaryStage;

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
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            statusLabel.setText("Processing: " + selectedFile.getName());
            try {
                int width = (int) resolutionSlider.getValue();
                String asciiArt = asciiEngine.convert(selectedFile, width);
                asciiTextArea.setText(asciiArt);
                statusLabel.setText("Done");
            } catch (IOException e) {
                statusLabel.setText("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
