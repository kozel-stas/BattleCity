package com.battlecity.models.blocks;

import com.battlecity.models.map.PhysicalObject;
import com.battlecity.models.properties.Destroyable;
import com.battlecity.models.properties.Drawable;
import com.battlecity.models.properties.GameProperties;
import com.battlecity.models.properties.Physical;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

public class Fortress implements Destroyable, Drawable, Physical {

    private final GameProperties gameProperties;
    private final PhysicalObject physicalObject;
    private transient final long owner;

    public Fortress(int coordinateX, int coordinateY, GameProperties gameProperties, long clientID) {
        this.physicalObject = new PhysicalObject(coordinateX, coordinateY, gameProperties.getBlockArea().getHeight(), gameProperties.getBlockArea().getWidth());
        this.gameProperties = gameProperties;
        this.owner = clientID;
    }

    @Override
    public long getId() {
        return getPhysicalObject().getId();
    }

    @Override
    public boolean destroyObject() {
        return true;
    }

    @Override
    public PaintListener draw(Canvas canvas) {
        PaintListener paintListener = new PaintListener() {
            @Override
            public void paintControl(PaintEvent paintEvent) {
                Rectangle rectangle = canvas.getBounds();
                paintEvent.gc.setForeground(new Color(null, 125, 125, 125));
                float proportionsY = rectangle.height / gameProperties.getMapArea().getHeight();
                float proportionsX = rectangle.width / gameProperties.getMapArea().getWidth();
                paintEvent.gc.drawRectangle(
                        (int) (proportionsX * physicalObject.getCoordinateX()),
                        (int) (proportionsY * physicalObject.getCoordinateY()),
                        (int) (proportionsX * physicalObject.getArea().getWidth()),
                        (int) (proportionsY * physicalObject.getArea().getHeight())
                );
                paintEvent.gc.drawOval(
                        (int) (proportionsX * physicalObject.getCoordinateX()),
                        (int) (proportionsY * physicalObject.getCoordinateY()),
                        (int) (proportionsX * physicalObject.getArea().getWidth()),
                        (int) (proportionsY * physicalObject.getArea().getHeight())
                );
            }
        };
        canvas.addPaintListener(paintListener);
        return paintListener;
    }

    @Override
    public void updateDataForDrawing(Drawable object) {
        // nothing to do
        // it's not movable
    }

    public long getOwner() {
        return owner;
    }

    @Override
    public PhysicalObject getPhysicalObject() {
        return physicalObject;
    }
}
