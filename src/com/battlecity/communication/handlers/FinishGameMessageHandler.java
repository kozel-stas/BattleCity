package com.battlecity.communication.handlers;

import com.battlecity.client.view.FinishWindow;
import com.battlecity.client.view.GameWindow;
import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.exceptions.MissingParamException;
import com.battlecity.communication.messages.Message;
import org.eclipse.swt.widgets.Display;

public class FinishGameMessageHandler implements MessageHandler {

    private final GameWindow gameWindow;

    public FinishGameMessageHandler(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    @Override
    public byte[] getType() {
        return MessageTypes.TYPE_FINISH;
    }

    @Override
    public void processMessage(Message message) throws MissingParamException {
        Display.getDefault().asyncExec(()->{
            new FinishWindow(Display.getDefault(), gameWindow.getShell());
            gameWindow.disposeWindow();
        });
    }

}
