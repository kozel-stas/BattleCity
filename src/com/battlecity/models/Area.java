package com.battlecity.models;

/**
 * x,y---|
 * |     |
 * |---x1,y1
 */
public class Area {

    private int coordinateX;
    private int coordinateY;
    private int height;
    private int width;

    public Area(int coordinateX, int coordinateY, int height, int width) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.height = height;
        this.width = width;
    }

    public int getCoordinateX1() {
        return coordinateX + width;
    }

    public int getCoordinateY1() {
        return coordinateY + height;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    protected void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    protected void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
