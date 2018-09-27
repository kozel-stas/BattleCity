package com.battlecity.models;

import com.battlecity.utils.IDGeneratorUtils;

import java.io.Serializable;

public class PhysicalObject implements Serializable {

    private long id;
    private Area area;

    public PhysicalObject(
            int coordinateX,
            int coordinateY,
            int height,
            int width
    ) {
        this.id = IDGeneratorUtils.generateID();
        this.area = new Area(coordinateX, coordinateY, height, width);
    }

    public int getCoordinateY1() {
        return area.getCoordinateY1();
    }

    public int getCoordinateX1() {
        return area.getCoordinateX1();
    }

    public int getCoordinateY() {
        return area.getCoordinateY();
    }

    protected void setCoordinateY(int coordinateY) {
        this.area.setCoordinateY(coordinateY);
    }

    public int getCoordinateX() {
        return area.getCoordinateX();
    }

    protected void setCoordinateX(int coordinateX) {
        this.area.setCoordinateX(coordinateX);
    }

    protected Area getArea(){
        return area;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof PhysicalObject) {
            PhysicalObject that = (PhysicalObject) obj;
            return that.getId() == getId();
        }
        return false;
    }

}
