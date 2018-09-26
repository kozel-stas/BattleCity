package com.battlecity.models.blocks;

import com.battlecity.models.*;
import com.battlecity.models.Iterable;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

public class Bullet extends PhysicalObject implements Destroyable, Iterable, Drawable {

    private final Disposition disposition;
    private final Tank owner;
    private final int speed;
    private final int step;
    private final MapSize mapSize;

    public Bullet(int coordinateX, int coordinateY, MapSize mapSize, Disposition disposition, Tank owner) {
        super(coordinateX, coordinateY, mapSize.getBulletArea().getHeight(), mapSize.getBulletArea().getWidth());
        this.mapSize = mapSize;
        this.disposition = disposition;
        this.owner = owner;
        this.speed = 3;
        this.step = 1;
    }

    @Override
    public void doIterate() {
        Area newArea = getAreaAfterIterate();
        setCoordinateX(newArea.getCoordinateX());
        setCoordinateY(newArea.getCoordinateY());
    }

    @Override
    public Area getAreaAfterIterate() {
        return this.disposition.getAreaAfterOffset(getArea(), step * speed);
    }

    @Override
    public boolean destroyObject() {
        // one bullet - one destroy
        return true;
    }

    public Tank getOwner() {
        return owner;
    }

    public Disposition getDisposition() {
        return disposition;
    }

    @Override
    public PaintListener draw(Canvas canvas) {
        PaintListener paintListener = new PaintListener() {
            @Override
            public void paintControl(PaintEvent paintEvent) {
                Rectangle rectangle = canvas.getBounds();
                paintEvent.gc.setForeground(new Color(null, 255, 255, 255));
                float proportionsY = rectangle.height / mapSize.getMapArea().getHeight();
                float proportionsX = rectangle.width / mapSize.getMapArea().getWidth();
                paintEvent.gc.drawRectangle((int) (proportionsX * getCoordinateX()),
                        (int) (proportionsY * getCoordinateY()),
                        (int) (proportionsX * getArea().getWidth()),
                        (int) (proportionsY * getArea().getHeight()));
            }
        };
        canvas.addPaintListener(paintListener);
        return paintListener;
    }

    @Override
    public void updateDataForDrawing(Drawable drawable) {
        Bullet bullet = (Bullet) drawable;
        setCoordinateX(bullet.getCoordinateX());
        setCoordinateY(bullet.getCoordinateY());
    }
}
