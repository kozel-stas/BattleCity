package com.battlecity.models.blocks;

import com.battlecity.models.Destroyable;
import com.battlecity.models.Drawable;
import com.battlecity.models.MapSize;
import com.battlecity.models.PhysicalObject;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

public class Fortress extends PhysicalObject implements Destroyable, Drawable {

    private final MapSize mapSize;
    private final long clientID;

    public Fortress(int coordinateX, int coordinateY, MapSize mapSize, long clientID) {
        super(coordinateX, coordinateY, mapSize.getBlockArea().getHeight(), mapSize.getBlockArea().getWidth());
        this.mapSize = mapSize;
        this.clientID = clientID;
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
    public void updateDataForDrawing(Drawable object) {
        // nothing to do
        // it's not movable
    }

    public long getClientID() {
        return clientID;
    }
}
