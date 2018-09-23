package com.battlecity.models;

import org.jetbrains.annotations.NotNull;

public enum Disposition {

    TOP,
    BOTTOM,
    LEFT,
    RIGHT,;

    @NotNull
    public Area getAreaAfterOffset(Area area, int offset) {
        Area newArea = null;
        switch (this) {
            case TOP:
                newArea = new Area(area.getCoordinateX(), area.getCoordinateY() - offset, area.getHeight(), area.getWidth());
                break;
            case BOTTOM:
                newArea = new Area(area.getCoordinateX(), area.getCoordinateY() + offset, area.getHeight(), area.getWidth());
                break;
            case LEFT:
                newArea = new Area(area.getCoordinateX() - offset, area.getCoordinateY(), area.getHeight(), area.getWidth());
                break;
            case RIGHT:
                newArea = new Area(area.getCoordinateX() + offset, area.getCoordinateY(), area.getHeight(), area.getWidth());
                break;
            default:
                // logic mistake
                newArea = new Area(area.getCoordinateX(), area.getCoordinateY(), area.getHeight(), area.getWidth());
                break;
        }
        return newArea;
    }

    public Area getAreaForBullet(Area tankArea, MapSize mapSize) {
        Area newArea = null;
        Area bulletArea = mapSize.getBulletArea();
        switch (this) {
            case TOP:
                newArea = new Area(tankArea.getCoordinateX() + tankArea.getWidth() / 2 - bulletArea.getWidth() / 2,
                        tankArea.getCoordinateY() - 2 - bulletArea.getHeight(), bulletArea.getHeight(), bulletArea.getWidth());
                break;
            case BOTTOM:
                newArea = new Area(tankArea.getCoordinateX() + tankArea.getWidth() / 2 - bulletArea.getWidth() / 2,
                        tankArea.getCoordinateY() + tankArea.getHeight() + 2, bulletArea.getHeight(), bulletArea.getWidth());
                break;
            case LEFT:
                newArea = new Area(tankArea.getCoordinateX() - 2 - bulletArea.getWidth(),
                        tankArea.getCoordinateY() + tankArea.getHeight() / 2 - bulletArea.getHeight() / 2, bulletArea.getHeight(), bulletArea.getWidth());
                break;
            case RIGHT:
                newArea = new Area(tankArea.getCoordinateX() + tankArea.getWidth() + 2,
                        tankArea.getCoordinateY() + tankArea.getHeight() / 2 - bulletArea.getHeight() / 2, bulletArea.getHeight(), bulletArea.getWidth());
            default:
                // logic mistake
                break;
        }
        return newArea;
    }

}
