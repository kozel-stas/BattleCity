package com.battlecity.models.blocks;

import com.battlecity.models.map.Area;
import com.battlecity.models.map.Disposition;
import com.battlecity.models.map.PhysicalObject;
import com.battlecity.models.properties.*;
import com.battlecity.models.properties.Iterable;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

public class Bullet implements Destroyable, Iterable, Drawable, Physical {

    private final Disposition disposition;
    private transient final Tank owner;
    private transient final int step;
    private final GameProperties gameProperties;
    private final PhysicalObject physicalObject;

    public Bullet(int coordinateX, int coordinateY, GameProperties gameProperties, Disposition disposition, Tank owner) {
        this.physicalObject = new PhysicalObject(coordinateX, coordinateY, gameProperties.getBulletArea().getHeight(), gameProperties.getBulletArea().getWidth());
        this.gameProperties = gameProperties;
        this.disposition = disposition;
        this.owner = owner;
        this.step = gameProperties.getBulletStep();
    }

    @Override
    public void doIterate() {
        Area newArea = getAreaAfterIterate();
        physicalObject.setCoordinateX(newArea.getCoordinateX());
        physicalObject.setCoordinateY(newArea.getCoordinateY());
    }

    @Override
    public Area getAreaAfterIterate() {
        return this.disposition.getAreaAfterOffset(physicalObject.getArea(), step);
    }

    @Override
    public long getId() {
        return getPhysicalObject().getId();
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
                paintEvent.gc.drawRectangle((int) (proportionsX * physicalObject.getCoordinateX()),
                        (int) (proportionsY * physicalObject.getCoordinateY()),
                        (int) (proportionsX * physicalObject.getArea().getWidth()),
                        (int) (proportionsY * physicalObject.getArea().getHeight()));
            }
        };
        canvas.addPaintListener(paintListener);
        return paintListener;
    }

    @Override
    public void updateDataForDrawing(Drawable drawable) {
        Bullet bullet = (Bullet) drawable;
        physicalObject.setCoordinateX(bullet.getPhysicalObject().getCoordinateX());
        physicalObject.setCoordinateY(bullet.getPhysicalObject().getCoordinateY());
    }

    @Override
    public PhysicalObject getPhysicalObject() {
        return physicalObject;
    }
}
