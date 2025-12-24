package com.vasu.pixelforge;

import javafx.scene.paint.Color;

public class AsciiPixel {
    private final char character;
    private final Color color;

    public AsciiPixel(char character, Color color) {
        this.character = character;
        this.color = color;
    }

    public char getCharacter() {
        return character;
    }

    public Color getColor() {
        return color;
    }
}
