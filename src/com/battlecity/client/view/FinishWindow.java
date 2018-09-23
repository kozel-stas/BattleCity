package com.battlecity.client.view;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class FinishWindow {

    private final Display display;
    private final Shell shell;
    private final Color color;

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

        this.shell.redraw();

        this.shell.open();
    }

}
