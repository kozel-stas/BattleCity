package com.battlecity.models;

public enum MapSize {

    STANDART(new Area(0, 0, 1000, 1000), new Area(0, 0, 100, 100),
            new Area(0, 0, 80, 80), new Area(0, 0, 10, 10)),
//    MEDIUM(mapArea, blockArea, tankArea, bulletArea),
//    LARGE(mapArea, blockArea, tankArea, bulletArea),
    ;

    private Area mapArea;
    private Area blockArea;
    private Area tankArea;
    private Area bulletArea;

    MapSize(Area mapArea, Area blockArea, Area tankArea, Area bulletArea) {
        this.mapArea = mapArea;
        this.blockArea = blockArea;
        this.tankArea = tankArea;
        this.bulletArea = bulletArea;
    }


    public Area getBlockArea() {
        return blockArea;
    }

    public Area getBulletArea() {
        return bulletArea;
    }

    public Area getMapArea() {
        return mapArea;
    }

    public Area getTankArea() {
        return tankArea;
    }

}
