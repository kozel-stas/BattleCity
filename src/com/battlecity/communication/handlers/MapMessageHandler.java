package com.battlecity.communication.handlers;

import com.battlecity.client.view.GameWindow;
import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.exceptions.MissingParamException;
import com.battlecity.communication.messages.Message;
import com.battlecity.models.properties.Drawable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapMessageHandler implements MessageHandler {

    private static final Logger LOG = Logger.getLogger(MapMessageHandler.class.getName());
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
            LOG.log(Level.INFO, "Message received");
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
