package com.battlecity.models.properties;

import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;

import java.io.Serializable;

public interface Drawable extends Serializable {

    long getId();

    PaintListener draw(Canvas canvas);

    void updateDataForDrawing(Drawable object);

}
