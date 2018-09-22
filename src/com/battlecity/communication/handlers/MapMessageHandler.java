package com.battlecity.communication.handlers;

import com.battlecity.client.view.GameWindow;
import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.exceptions.MissingParamException;
import com.battlecity.communication.messages.Message;
import com.battlecity.models.Drawable;
import com.battlecity.models.GameMap;
import com.battlecity.models.PhysicalObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            System.out.println("Start process Map");
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(
                    message.getRequiredProperty(MessageTypes.KEY_GAME_MAP))
            );

            Object o = objectInputStream.readObject();
            if (o instanceof GameMap) {
                GameMap gameMap = (GameMap) o;
                Map<Long,Drawable> map = new HashMap<>();
                for (Map.Entry<Long, PhysicalObject> entry: gameMap.getPhysicalObjects().entrySet()){
                    if (entry.getValue() instanceof Drawable){
                        map.put(entry.getKey(),(Drawable) entry.getValue());
                    }
                }
                gameWindow.updateCanvas(map);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
