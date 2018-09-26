package com.battlecity.models.blocks;

import com.battlecity.models.*;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Tank extends PhysicalObject implements Movable, Destroyable, Drawable {

    private final AtomicInteger lives;
    private Disposition disposition;
    private transient final int speed;
    private transient final int step;
    private MapSize mapSize;
    private transient Future<?> moveTask;

    public Tank(int coordinateX, int coordinateY, MapSize mapSize) {
        super(coordinateX, coordinateY, mapSize.getTankArea().getHeight(), mapSize.getTankArea().getWidth());
        lives = new AtomicInteger(mapSize.getTankLives());
        disposition = Disposition.TOP; // default value
        step = mapSize.getTankStep();
        speed = mapSize.getTankSpeed();
        this.mapSize = mapSize;
    }

    @Override
    public boolean destroyObject() {
        return lives.decrementAndGet() == 0;
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
        return this.disposition.getAreaAfterOffset(getArea(), step);
    }

    public Bullet doShoot() {
        Area area = this.disposition.getAreaForBullet(getArea(), mapSize);
        return new Bullet(area.getCoordinateX(), area.getCoordinateY(), mapSize, this.disposition, this);
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
        Tank tank = (Tank) drawable;
        this.disposition = tank.disposition;
        setCoordinateX(tank.getCoordinateX());
        setCoordinateY(tank.getCoordinateY());
    }

    public Future<?> getMoveTask() {
        return moveTask;
    }

    public void setMoveTask(Future<?> moveTask) {
        this.moveTask = moveTask;
    }

    public int getSpeed() {
        return speed;
    }

}
