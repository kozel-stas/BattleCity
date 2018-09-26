package com.battlecity.models.blocks;

import com.battlecity.models.Destroyable;
import com.battlecity.models.Drawable;
import com.battlecity.models.GameProperties;
import com.battlecity.models.PhysicalObject;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

public class Fortress extends PhysicalObject implements Destroyable, Drawable {

    private final GameProperties gameProperties;
    private transient final long owner;

    public Fortress(int coordinateX, int coordinateY, GameProperties gameProperties, long clientID) {
        super(coordinateX, coordinateY, gameProperties.getBlockArea().getHeight(), gameProperties.getBlockArea().getWidth());
        this.gameProperties = gameProperties;
        this.owner = clientID;
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
                        (int) (proportionsX * getCoordinateX()),
                        (int) (proportionsY * getCoordinateY()),
                        (int) (proportionsX * getArea().getWidth()),
                        (int) (proportionsY * getArea().getHeight())
                );
                paintEvent.gc.drawOval(
                        (int) (proportionsX * getCoordinateX()),
                        (int) (proportionsY * getCoordinateY()),
                        (int) (proportionsX * getArea().getWidth()),
                        (int) (proportionsY * getArea().getHeight())
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

}
