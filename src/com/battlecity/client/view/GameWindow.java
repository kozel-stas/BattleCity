package com.battlecity.client.view;

import com.battlecity.client.ClientApplication;
import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.messages.Message;
import com.battlecity.utils.MapGeneratorUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.IOException;

public class GameWindow {
    private ClientApplication clientApplication;
    private Display display;
    private Shell shell;
    private Color color;
    private Canvas canvas;

    public GameWindow(Display display, Shell shell, ClientApplication clientApplication) {
        this.clientApplication = clientApplication;
        this.display = display;
        this.shell = shell;
        color = new Color(null, 19, 0, 26);
        this.shell.setText("BattleCity");
        this.shell.setSize(1000, 1000);
        this.shell.setBackground(color);
        this.shell.setFocus();
        this.shell.setModified(false);
        this.shell.setLocation(shell.getLocation());

        initCanvas();

        this.shell.redraw();
    }

    private void initCanvas() {
        try {
            clientApplication.getMessageSender().sendMessage(new Message(MessageTypes.TYPE_MOVE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        canvas = new Canvas(shell, SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED);
        canvas.setSize(1000, 1000);
        canvas.setBackground(color);

        canvas.addMouseMoveListener(new MouseMoveListener() {

            @Override
            public void mouseMove(MouseEvent mouseEvent) {

            }

        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {

            }
        });

        canvas.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

            }
        });

        canvas.setFocus();
    }
}
