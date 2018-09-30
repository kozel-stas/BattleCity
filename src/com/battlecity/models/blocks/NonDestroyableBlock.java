package com.battlecity.models.blocks;

import com.battlecity.models.map.PhysicalObject;
import com.battlecity.models.properties.Drawable;
import com.battlecity.models.properties.GameProperties;
import com.battlecity.models.properties.Physical;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

public class NonDestroyableBlock implements Drawable, Physical {

    private final GameProperties gameProperties;
    private final PhysicalObject physicalObject;

    public NonDestroyableBlock(int coordinateX, int coordinateY, GameProperties gameProperties) {
        this.physicalObject = new PhysicalObject(coordinateX, coordinateY, gameProperties.getBlockArea().getHeight(), gameProperties.getBlockArea().getWidth());
        this.gameProperties = gameProperties;
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
                paintEvent.gc.drawRectangle((int) (proportionsX * getPhysicalObject().getCoordinateX()),
                        (int) (proportionsY * getPhysicalObject().getCoordinateY()),
                        (int) (proportionsX * getPhysicalObject().getArea().getWidth()),
                        (int) (proportionsY * getPhysicalObject().getArea().getHeight()));
                paintEvent.gc.drawLine(
                        (int) (proportionsX * getPhysicalObject().getCoordinateX()),
                        (int) (proportionsY * getPhysicalObject().getCoordinateY()),
                        (int) (proportionsX * getPhysicalObject().getCoordinateX1()),
                        (int) (proportionsY * getPhysicalObject().getCoordinateY1())
                );
                paintEvent.gc.drawLine(
                        (int) (proportionsX * getPhysicalObject().getCoordinateX1()),
                        (int) (proportionsY * getPhysicalObject().getCoordinateY()),
                        (int) (proportionsX * getPhysicalObject().getCoordinateX()),
                        (int) (proportionsY * getPhysicalObject().getCoordinateY1())
                );
                paintEvent.gc.drawOval(
                        (int) (proportionsX * getPhysicalObject().getCoordinateX()),
                        (int) (proportionsY * getPhysicalObject().getCoordinateY()),
                        (int) (proportionsX * getPhysicalObject().getArea().getWidth()),
                        (int) (proportionsY * getPhysicalObject().getArea().getHeight())
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
