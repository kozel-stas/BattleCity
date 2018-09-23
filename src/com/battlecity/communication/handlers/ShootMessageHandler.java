package com.battlecity.communication.handlers;

import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.exceptions.MissingParamException;
import com.battlecity.communication.messages.Message;
import com.battlecity.models.blocks.Bullet;
import com.battlecity.models.blocks.Tank;
import com.battlecity.server.controllers.GamesMgr;
import com.battlecity.server.controllers.MessageServer;
import com.battlecity.utils.BytesUtils;

public class ShootMessageHandler implements MessageHandler {

    private GamesMgr gamesMgr;
    private MessageServer messageServer;

    public ShootMessageHandler(GamesMgr gamesMgr, MessageServer messageServer) {
        this.gamesMgr = gamesMgr;
        this.messageServer = messageServer;
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
                System.out.println(game.getGameMap().addPhysicalObjectToMap(bullet));
                game.sendMapToClients();
            } else {
                // new live, spawn new tank
                game.tryToRespawnTank();
            }
        });
    }
}
