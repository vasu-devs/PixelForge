package com.vasu.pixelforge;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AsciiEngine {

    // Detailed density string for high-quality ASCII art
    private static final String DENSITY = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ";

    public String convert(File imageFile, int width) throws IOException {
        BufferedImage originalImage = ImageIO.read(imageFile);
        if (originalImage == null) {
            throw new IOException("Could not load image: " + imageFile.getName());
        }

        // Calculate height to maintain aspect ratio, but we will handle the font aspect
        // ratio in the loop
        // Standard fonts are usually about twice as tall as they are wide.
        // So we need to sample pixels such that we counteract this.
        // One way is to resize the image to (width, height * 0.5) effectively squashing
        // it,
        // so when displayed with tall characters it looks normal.

        double aspectRatio = (double) originalImage.getHeight() / originalImage.getWidth();
        int newHeight = (int) (width * aspectRatio * 0.5); // 0.5 correction factor for font aspect ratio

        if (newHeight < 1)
            newHeight = 1;

        BufferedImage resizedImage = new BufferedImage(width, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, newHeight, null);
        g.dispose();

        StringBuilder ascii = new StringBuilder();
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = resizedImage.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                int gray = (red + green + blue) / 3;

                // Map gray value (0-255) to density string index
                // 0 (black) -> last char (space), 255 (white) -> first char ($) ?
                // Usually dark pixels should be dense characters.
                // If 0 is black, we want dense char.
                // If 255 is white, we want light char.
                // The string starts with '$' (dense) and ends with ' ' (light).
                // So 0 -> index 0, 255 -> index length-1.
                // But wait, usually 0 is black.
                // Let's check the string: "$..." is dense. " " is light.
                // If we want black background and green text (Matrix style),
                // then "light" pixels in the image should probably be represented by
                // characters?
                // Or "dark" pixels?
                // Standard ASCII art: Darker pixel = Denser character.
                // So 0 (black) -> '$', 255 (white) -> ' '.
                // But if we display this on a BLACK background with GREEN text:
                // Then '$' will be a lot of green pixels (bright).
                // So a dark area in the original image will become BRIGHT in the ASCII art?
                // That's an inverted image.
                // If the original image has a black background, we want it to be black (space).
                // So 0 (black) -> ' ' (space).
                // 255 (white) -> '$' (dense).
                // So we need to invert the mapping.

                // Let's assume standard behavior first: Dark = Dense.
                // If I take a photo of a face, shadows are dark.
                // If I print it on white paper, shadows are dense ink ($).
                // If I display it on black screen with green text, shadows ($) are BRIGHT
                // GREEN.
                // So the face looks like a negative.

                // To look correct on a black background (Matrix style), we usually want:
                // Bright parts of image -> Dense characters (more green pixels).
                // Dark parts of image -> Light characters (fewer green pixels/space).

                // So: 0 (black) -> ' ' (index length-1).
                // 255 (white) -> '$' (index 0).

                // Let's implement this "Matrix/Dark Mode" mapping.
                // gray 0 -> index length-1
                // gray 255 -> index 0

                int index = (int) ((255 - gray) / 255.0 * (DENSITY.length() - 1));

                // Clamp index just in case
                if (index < 0)
                    index = 0;
                if (index >= DENSITY.length())
                    index = DENSITY.length() - 1;

                ascii.append(DENSITY.charAt(index));
            }
            ascii.append("\n");
        }
        return ascii.toString();
    }
}
