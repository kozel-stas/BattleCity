package com.battlecity.client.view;

import com.battlecity.client.controllers.ClientApplication;
import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.messages.Message;
import com.battlecity.models.Disposition;
import com.battlecity.models.Drawable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameWindow {
    private ClientApplication clientApplication;
    private Display display;
    private Shell shell;
    private Color color;
    private Canvas canvas;

    private final Map<Long,PaintListener> paintListeners = new HashMap<>();
    private final Map<Long, Drawable> drawables = new HashMap<>();

    public GameWindow(Display display, Shell shell) {
        this.display = display;
        this.shell = shell;
        color = new Color(null, 19, 0, 26);
        this.shell.setText("BattleCity");
        this.shell.setSize(1000, 1000);
        this.shell.setBackground(color);
        this.shell.setFocus();
        this.shell.setModified(false);
        this.shell.setLocation(shell.getLocation());

        this.clientApplication = new ClientApplication(this);

        initCanvas();

        this.shell.redraw();


        shell.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent e) {
                clientApplication.getMessageSender().shutdown();
            }
        });
    }

    private void initCanvas() {
        try {
            clientApplication.getMessageSender().sendMessage(new Message(MessageTypes.TYPE_AVAILABILITY));
        } catch (IOException e) {
            e.printStackTrace();
        }
        canvas = new Canvas(shell, SWT.NULL);
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
                try {
                    System.out.println(e.keyCode);
                    Message message;
                    switch (e.keyCode) {
                        case 16777220:
                            message = new Message(MessageTypes.TYPE_MOVE);
                            message.setProperty(MessageTypes.KEY_DISPOSITION, Disposition.RIGHT.toString().getBytes());
                            clientApplication.getMessageSender().sendMessage(message);
                            break;
                        case 16777219:
                            message = new Message(MessageTypes.TYPE_MOVE);
                            message.setProperty(MessageTypes.KEY_DISPOSITION, Disposition.LEFT.toString().getBytes());
                            clientApplication.getMessageSender().sendMessage(message);
                            break;
                        case 16777218:
                            message = new Message(MessageTypes.TYPE_MOVE);
                            message.setProperty(MessageTypes.KEY_DISPOSITION, Disposition.BOTTOM.toString().getBytes());
                            clientApplication.getMessageSender().sendMessage(message);
                            break;
                        case 16777217:
                            message = new Message(MessageTypes.TYPE_MOVE);
                            message.setProperty(MessageTypes.KEY_DISPOSITION, Disposition.TOP.toString().getBytes());
                            clientApplication.getMessageSender().sendMessage(message);
                            break;
                    }
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        });

        canvas.setFocus();
    }

    public void updateCanvas(Map<Long, Drawable> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        display.asyncExec(() -> {
            drawables.forEach((id, drawable) -> {
                Drawable currDrawable = map.remove(id);
                if (currDrawable==null){
                    drawables.remove(id);
                    paintListeners.remove(id);
                }
                drawable.updateDataForDrawing(currDrawable);
            });
            map.forEach((id, drawable)->{
                drawables.put(id,drawable);
                paintListeners.put(id, drawable.draw(canvas));
            });
            canvas.redraw();
        });
    }

}
