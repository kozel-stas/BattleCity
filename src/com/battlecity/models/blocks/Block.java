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

import java.util.concurrent.atomic.AtomicInteger;

public class Block implements Drawable, Destroyable, Physical {

    private final GameProperties gameProperties;
    private final PhysicalObject physicalObject;
    private transient final AtomicInteger lives;

    public Block(int coordinateX, int coordinateY, GameProperties gameProperties) {
        this.physicalObject = new PhysicalObject(coordinateX, coordinateY, gameProperties.getBlockArea().getHeight(), gameProperties.getBlockArea().getWidth());
        this.gameProperties = gameProperties;
        this.lives = new AtomicInteger(gameProperties.getBlockLives());
    }

    @Override
    public boolean destroyObject() {
        return lives.decrementAndGet() == 0;
    }

    @Override
    public long getId() {
        return getPhysicalObject().getId();
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
                paintEvent.gc.drawLine(
                        (int) (proportionsX * physicalObject.getCoordinateX()),
                        (int) (proportionsY * physicalObject.getCoordinateY()),
                        (int) (proportionsX * physicalObject.getCoordinateX1()),
                        (int) (proportionsY * physicalObject.getCoordinateY1())
                );
                paintEvent.gc.drawLine(
                        (int) (proportionsX * physicalObject.getCoordinateX1()),
                        (int) (proportionsY * physicalObject.getCoordinateY()),
                        (int) (proportionsX * physicalObject.getCoordinateX()),
                        (int) (proportionsY * physicalObject.getCoordinateY1())
                );
            }
        };
        canvas.addPaintListener(paintListener);
        return paintListener;
    }

    @Override
    public void updateDataForDrawing(Drawable object) {
        //can't update / not movable
    }

    @Override
    public PhysicalObject getPhysicalObject() {
        return physicalObject;
    }
}
