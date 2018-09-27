package com.battlecity.communication.handlers;

import com.battlecity.client.view.GameWindow;
import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.exceptions.MissingParamException;
import com.battlecity.communication.messages.Message;
import com.battlecity.models.Drawable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ConcurrentSkipListMap;

public class MapMessageHandler implements MessageHandler {

    private final GameWindow gameWindow;

    public MapMessageHandler(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    @Override
    public byte[] getType() {
        return MessageTypes.TYPE_MAP;
    }

    @Override
    public void processMessage(Message message) throws MissingParamException {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(
                    message.getRequiredProperty(MessageTypes.KEY_GAME_MAP))
            );

            Object o = objectInputStream.readObject();
            if (o instanceof ConcurrentSkipListMap) {
                ConcurrentSkipListMap<Long, Drawable> gameMap = (ConcurrentSkipListMap<Long, Drawable>) o;
                gameWindow.updateCanvas(gameMap);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
