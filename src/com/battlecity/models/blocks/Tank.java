package com.battlecity.models.blocks;

import com.battlecity.models.*;

import java.util.concurrent.atomic.AtomicInteger;

public class Tank extends PhysicalObject implements Movable, Destroyable {

    private final AtomicInteger lives;
    private Disposition disposition;
    private final int speed;
    private MapSize mapSize;

    public Tank(int coordinateX, int coordinateY, MapSize mapSize) {
        super(coordinateX, coordinateY, mapSize.getTankArea().getHeight(), mapSize.getTankArea().getWidth());
        lives = new AtomicInteger(1);
        disposition = Disposition.TOP; // default value
        speed = 2; //default value
        this.mapSize = mapSize;
    }

    @Override
    public boolean destroyObject() {
        if (lives.decrementAndGet() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void move(Disposition disposition) {
        if (this.disposition == disposition) {
            Area area = getAreaAfterMove(disposition);
            setCoordinateX(area.getCoordinateX());
            setCoordinateY(area.getCoordinateY());
        } else {
            this.disposition = disposition;
        }
    }

    @Override
    public Area getAreaAfterMove(Disposition disposition) {
        if (this.disposition != disposition) {
            return getArea();
        }
        return this.disposition.getAreaAfterOffset(getArea(), speed);
    }

    public Bullet doShoot() {
        Area area = this.disposition.getAreaForBullet(getArea(), mapSize);
        return new Bullet(area.getCoordinateX(), area.getCoordinateY(), area.getHeight(), area.getWidth(), this.disposition, this);
    }

    public Disposition getDisposition() {
        return disposition;
    }
}
