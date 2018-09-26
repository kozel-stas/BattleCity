package com.battlecity.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class FinishWindow {

    private final Display display;
    private final Shell shell;
    private final Color color;
    private Canvas canvas;

    public FinishWindow(Display display, Shell shell) {
        this.display = display;
        this.shell = shell;

        color = new Color(null, 19, 0, 26);
        this.shell.setText("BattleCity");
        this.shell.setSize(1020, 1050);
        this.shell.setBackground(color);
        this.shell.setFocus();
        this.shell.setModified(false);
        this.shell.setLocation(shell.getLocation());

        initCanvas();

        this.shell.redraw();
    }

    private void initCanvas(){
        canvas = new Canvas(shell, SWT.NULL);
        canvas.setSize(1000, 1000);
        canvas.setBackground(color);

        canvas.addPaintListener(new PaintListener() {

            @Override
            public void paintControl(PaintEvent paintEvent) {
                Font font = new Font(display, "Arial", 20, SWT.BOLD | SWT.ITALIC);
                paintEvent.gc.setFont(font);
                paintEvent.gc.setForeground(new Color(null, 255, 255, 102));
                paintEvent.gc.drawText("FINISH", 460, 450);
            }

        });
    }

}
