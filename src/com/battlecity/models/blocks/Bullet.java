package com.battlecity.models.blocks;

import com.battlecity.models.*;
import com.battlecity.models.Iterable;

public class Bullet extends PhysicalObject implements Destroyable, Iterable {

    private final Disposition disposition;
    private final Tank owner;
    private final int speed;

    public Bullet(int coordinateX, int coordinateY, int height, int width, Disposition disposition, Tank owner) {
        super(coordinateX, coordinateY, height, width);
        this.disposition = disposition;
        this.owner = owner;
        this.speed = 4;
    }

    @Override
    public void doIterate() {
        Area newArea = getAreaAfterIterate();
        setCoordinateX(newArea.getCoordinateX());
        setCoordinateY(newArea.getCoordinateY());
    }

    @Override
    public Area getAreaAfterIterate() {
        return this.disposition.getAreaAfterOffset(getArea(), speed);
    }

    @Override
    public boolean destroyObject() {
        // one shoot - one destroy
        return true;
    }

    public Tank getOwner() {
        return owner;
    }

    public Disposition getDisposition() {
        return disposition;
    }
}
