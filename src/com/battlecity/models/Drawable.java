package com.battlecity.models;

import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;

public interface Drawable {

    long getId();

    PaintListener draw(Canvas canvas);

    void updateDataForDrawing(Drawable object);

}
