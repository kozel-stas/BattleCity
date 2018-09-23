package com.battlecity.client.view;

import com.battlecity.client.controllers.ClientApplication;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainWindow {

    private Display display;
    private Shell shell;
    private Color color;
    private Canvas canvas;
    private PaintListener textStartGame;
    private PaintListener textExit;

    private Color checkedColor;
    private Color uncheckedColor;
    private Color textStartGameColor;
    private Color textExitColor;

    private int currentPosition = 1;

    public MainWindow() {
        display = new Display();
        shell = new Shell(display, SWT.TITLE | SWT.CLOSE | SWT.DOUBLE_BUFFERED);
        color = new Color(null, 19, 0, 26);
        shell.setText("BattleCity");
        shell.setSize(1020, 1000);
        shell.setBackground(color);
        shell.setFocus();
        shell.setModified(false);

        checkedColor = new Color(null, 255, 148, 77);
        uncheckedColor = new Color(null, 255, 255, 102);
        textStartGameColor = uncheckedColor;
        textExitColor = uncheckedColor;

        initCanvas();

        centerWindow();

        shell.open();
        try {
            while (!shell.isDisposed()) {
                if (display.readAndDispatch()) {
                    display.sleep();
                }
            }
            display.dispose();
        } catch (SWTException e) {
            // ignore
        }
    }

    private void centerWindow() {
        Rectangle rectangle = shell.getDisplay().getBounds();
        Point p = shell.getSize();
        int nLeft = (rectangle.width - p.x) / 2;
        int nTop = (rectangle.height - p.y) / 2;
        shell.setBounds(nLeft, nTop, p.x, p.y);
    }

    private void initCanvas() {
        canvas = new Canvas(shell, SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED);
        canvas.setSize(1000, 1000);
        canvas.setBackground(color);

        canvas.addMouseMoveListener(new MouseMoveListener() {

            @Override
            public void mouseMove(MouseEvent mouseEvent) {
                if (mouseEvent.x > 400 && mouseEvent.x < 600 && mouseEvent.y > 400 && mouseEvent.y < 450) {
                    currentPosition = 2;
                    redrawText();
                }
                if (mouseEvent.x > 400 && mouseEvent.x < 600 && mouseEvent.y > 450 && mouseEvent.y < 500) {
                    currentPosition = 1;
                    redrawText();
                }
            }

        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                if (e.button == 1) {
                    chooseEvent();
                }
            }
        });

        canvas.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.keyCode) {
                    case 16777217:
                        currentPosition++;
                        redrawText();
                        break;
                    case 16777218:
                        currentPosition--;
                        redrawText();
                        break;
                    case 13:
                        chooseEvent();
                        break;
                }

            }
        });

        initText();

        canvas.setFocus();
        canvas.addPaintListener(textStartGame);
        canvas.addPaintListener(textExit);
    }

    private void initText() {
        textStartGame = new PaintListener() {

            @Override
            public void paintControl(PaintEvent paintEvent) {
                Font font = new Font(display, "Arial", 20, SWT.BOLD | SWT.ITALIC);
                paintEvent.gc.setFont(font);
                paintEvent.gc.setForeground(textStartGameColor);
                paintEvent.gc.drawText("Start", 460, 400);
            }

        };

        textExit = new PaintListener() {

            @Override
            public void paintControl(PaintEvent paintEvent) {
                Font font = new Font(display, "Arial", 20, SWT.BOLD | SWT.ITALIC);
                paintEvent.gc.setFont(font);
                paintEvent.gc.setForeground(textExitColor);
                paintEvent.gc.drawText("Exit", 460, 450);
            }

        };
    }

    private void redrawText() {
        if (currentPosition % 2 == 0) {
            textStartGameColor = checkedColor;
            textExitColor = uncheckedColor;
        } else {
            textExitColor = checkedColor;
            textStartGameColor = uncheckedColor;
        }
        canvas.redraw();
    }

    private void chooseEvent() {
        if (currentPosition % 2 == 0) {
            new GameWindow(display, shell);
            canvas.dispose();
        } else {
            shell.close();
            display.close();
        }
    }

}
