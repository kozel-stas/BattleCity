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

import java.util.concurrent.atomic.AtomicInteger;

public class Block extends PhysicalObject implements Drawable, Destroyable {

    private final GameProperties gameProperties;
    private transient final AtomicInteger lives;

    public Block(int coordinateX, int coordinateY, GameProperties gameProperties) {
        super(coordinateX, coordinateY, gameProperties.getBlockArea().getHeight(), gameProperties.getBlockArea().getWidth());
        this.gameProperties = gameProperties;
        this.lives = new AtomicInteger(gameProperties.getBlockLives());
    }

    @Override
    public boolean destroyObject() {
        return lives.decrementAndGet() == 0;
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
                paintEvent.gc.drawLine(
                        (int) (proportionsX * getCoordinateX()),
                        (int) (proportionsY * getCoordinateY()),
                        (int) (proportionsX * getCoordinateX1()),
                        (int) (proportionsY * getCoordinateY1())
                );
                paintEvent.gc.drawLine(
                        (int) (proportionsX * getCoordinateX1()),
                        (int) (proportionsY * getCoordinateY()),
                        (int) (proportionsX * getCoordinateX()),
                        (int) (proportionsY * getCoordinateY1())
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

}
