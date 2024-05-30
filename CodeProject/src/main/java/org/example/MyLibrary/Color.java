package org.example.MyLibrary;

public enum Color {
    OFF(0,0,0),
    ON(255,192,203),
    RED(255,0,0),
    LIME(0,255,0),
    BLUE(0,0,255),
    YELLOW(255,255,0),
    CYAN(0,255,255),
    MAGENTA(255,0,255),
    SILVER(192,192,192),
    GRAY(128,128,128),
    PURPLE(128,0,128),
    TEAL(0,128,128),
    NAVY(0,0,128),
    GREEN(0,128,0),
    MAROON(128,0,0),
    BROWN(165,42,42),
    ORANGE(255,140,0),
    GOLD(255,215,0),
    DARKGREEN(0,100,0),
    WHITE(255,255,255);

    private final int r;
    private final int g;
    private final int b;

    Color(final int r, final int g, final int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }
}