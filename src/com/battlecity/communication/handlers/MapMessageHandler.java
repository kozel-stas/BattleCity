package com.battlecity.communication.handlers;

import com.battlecity.client.controllers.MapConverterController;
import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.exceptions.MissingParamException;
import com.battlecity.communication.messages.Message;
import com.battlecity.models.GameMap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MapMessageHandler implements MessageHandler {

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
            if (o instanceof GameMap) {
                GameMap gameMap = (GameMap) o;
                MapConverterController.getInstance().processInputMap(gameMap);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
