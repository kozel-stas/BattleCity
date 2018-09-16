package com.battlecity.communication.handlers;

import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.exceptions.MissingParamException;
import com.battlecity.communication.messages.Message;
import com.battlecity.models.Area;
import com.battlecity.models.Disposition;
import com.battlecity.models.PhysicalObject;
import com.battlecity.models.blocks.Tank;
import com.battlecity.server.controllers.GamesMgr;
import com.battlecity.server.controllers.MessageServer;
import com.battlecity.server.model.ClientConnection;
import com.battlecity.utils.BytesUtils;
import com.battlecity.utils.CollusionUtils;

public class MoveMessageHandler implements MessageHandler {

    private GamesMgr gamesMgr;
    private MessageServer messageServer;

    public MoveMessageHandler(GamesMgr gamesMgr, MessageServer messageServer) {
        this.gamesMgr = gamesMgr;
        this.messageServer = messageServer;
    }

    @Override
    public byte[] getType() {
        return MessageTypes.TYPE_MOVE;
    }

    @Override
    public void processMessage(Message message) throws MissingParamException {
        System.out.println("MoveMessageHandler: Start process message " + message);

        long clientId = BytesUtils.byteArrayToLong(message.getRequiredProperty(MessageTypes.KEY_CONNECTION_ID));
        Disposition disposition = Disposition.valueOf(BytesUtils.byteArrayToString(message.getRequiredProperty(MessageTypes.KEY_DISPOSITION)));
        gamesMgr.executeSynchronized(clientId, (game) -> {
            ClientConnection clientConnection = game.getClients().get(clientId);
            Tank tank = game.getTank(clientId);
            if (tank != null) {
                Area areaAfterMove = tank.getAreaAfterMove(disposition);
                PhysicalObject physicalObject = game.getPhysicalObject(areaAfterMove);
                if (physicalObject == null && CollusionUtils.checkCollusion(game.getGameMap(), areaAfterMove)) {
                    tank.move(disposition);
                    // publish event
                }
            } else {
                // new live, spawn new tank
                game.tryToRespawnTank();
            }
        });
    }
}
