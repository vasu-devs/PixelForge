package com.vasu.pixelforge;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AsciiEngine {

    // Detailed density string for high-quality ASCII art
    private static final String DENSITY = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ";

    public AsciiPixel[][] convertToPixels(File imageFile, int width) throws IOException {
        BufferedImage originalImage = ImageIO.read(imageFile);
        if (originalImage == null) {
            throw new IOException("Could not load image: " + imageFile.getName());
        }

        double aspectRatio = (double) originalImage.getHeight() / originalImage.getWidth();
        int newHeight = (int) (width * aspectRatio * 0.5);

        if (newHeight < 1)
            newHeight = 1;

        BufferedImage resizedImage = new BufferedImage(width, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, newHeight, null);
        g.dispose();

        AsciiPixel[][] pixels = new AsciiPixel[newHeight][width];
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = resizedImage.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                int gray = (red + green + blue) / 3;

                int index = (int) ((255 - gray) / 255.0 * (DENSITY.length() - 1));
                if (index < 0)
                    index = 0;
                if (index >= DENSITY.length())
                    index = DENSITY.length() - 1;

                char c = DENSITY.charAt(index);
                javafx.scene.paint.Color color = javafx.scene.paint.Color.rgb(red, green, blue);
                pixels[y][x] = new AsciiPixel(c, color);
            }
        }
        return pixels;
    }

    // Keep the old convert method for backward compatibility or simple text output
    public String convert(File imageFile, int width) throws IOException {
        AsciiPixel[][] pixels = convertToPixels(imageFile, width);
        StringBuilder sb = new StringBuilder();
        for (AsciiPixel[] row : pixels) {
            for (AsciiPixel pixel : row) {
                sb.append(pixel.getCharacter());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
