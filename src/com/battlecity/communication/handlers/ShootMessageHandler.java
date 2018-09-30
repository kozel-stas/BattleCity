package com.battlecity.communication.handlers;

import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.exceptions.MissingParamException;
import com.battlecity.communication.messages.Message;
import com.battlecity.models.blocks.Bullet;
import com.battlecity.models.blocks.Tank;
import com.battlecity.models.properties.Physical;
import com.battlecity.server.controllers.GamesMgr;
import com.battlecity.utils.BytesUtils;

public class ShootMessageHandler implements MessageHandler {

    private GamesMgr gamesMgr;

    public ShootMessageHandler(GamesMgr gamesMgr) {
        this.gamesMgr = gamesMgr;
    }

    @Override
    public byte[] getType() {
        return MessageTypes.TYPE_SHOOT;
    }

    @Override
    public void processMessage(Message message) throws MissingParamException {
        long clientId = BytesUtils.byteArrayToLong(message.getRequiredProperty(MessageTypes.KEY_CONNECTION_ID));
        gamesMgr.executeSynchronized(clientId, (game) -> {
            Tank tank = game.getTank(clientId);
            if (tank != null) {
                Bullet bullet = tank.doShoot();
                if (bullet != null && !game.getGameMap().addPhysicalObjectToMap(bullet)) {
                    Physical physicalObject = game.getGameMap().getPhysicalObject(bullet);
                    game.processConflictPhysicalObject(physicalObject);
                }
            } else {
                // new live, spawn new tank
                game.tryToRespawnTank();
            }
        });
    }
}
