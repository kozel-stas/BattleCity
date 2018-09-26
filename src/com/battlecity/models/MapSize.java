package com.battlecity.models;

public enum MapSize {

    STANDART(
            new Area(0, 0, 1000, 1000),
            new Area(0, 0, 100, 100),
            new Area(0, 0, 80, 80),
            new Area(0, 0, 10, 10),
            1,
            1,
            2,
            5,
            3
    ),
//    MEDIUM(mapArea, blockArea, tankArea, bulletArea),
//    LARGE(mapArea, blockArea, tankArea, bulletArea),
    ;

    private final Area mapArea;
    private final Area blockArea;
    private final Area tankArea;
    private final Area bulletArea;
    private final int tankLives;
    private final int blockLives;
    private final int tankStep;
    private final int tankSpeed;
    private final int bulletStep;

    MapSize(Area mapArea,
            Area blockArea,
            Area tankArea,
            Area bulletArea,
            int tankLives,
            int blockLives,
            int tankStep,
            int tankSpeed,
            int bulletStep
    ) {
        this.mapArea = mapArea;
        this.blockArea = blockArea;
        this.tankArea = tankArea;
        this.bulletArea = bulletArea;
        this.tankLives = tankLives;
        this.blockLives = blockLives;
        this.tankStep = tankStep;
        this.tankSpeed = tankSpeed;
        this.bulletStep = bulletStep;
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

    public int getTankLives() {
        return tankLives;
    }

    public int getBlockLives() {
        return blockLives;
    }

    public int getBulletStep() {
        return bulletStep;
    }

    public int getTankSpeed() {
        return tankSpeed;
    }

    public int getTankStep() {
        return tankStep;
    }

}
