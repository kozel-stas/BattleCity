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
    private transient final Tank owner;
    private transient final int step;
    private final GameProperties gameProperties;

    public Bullet(int coordinateX, int coordinateY, GameProperties gameProperties, Disposition disposition, Tank owner) {
        super(coordinateX, coordinateY, gameProperties.getBulletArea().getHeight(), gameProperties.getBulletArea().getWidth());
        this.gameProperties = gameProperties;
        this.disposition = disposition;
        this.owner = owner;
        this.step = gameProperties.getBulletStep();
    }

    @Override
    public void doIterate() {
        Area newArea = getAreaAfterIterate();
        setCoordinateX(newArea.getCoordinateX());
        setCoordinateY(newArea.getCoordinateY());
    }

    @Override
    public Area getAreaAfterIterate() {
        return this.disposition.getAreaAfterOffset(getArea(), step);
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
                float proportionsY = rectangle.height / gameProperties.getMapArea().getHeight();
                float proportionsX = rectangle.width / gameProperties.getMapArea().getWidth();
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
