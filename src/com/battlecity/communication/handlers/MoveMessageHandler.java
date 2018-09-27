package com.battlecity.communication.handlers;

import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.exceptions.MissingParamException;
import com.battlecity.communication.messages.Message;
import com.battlecity.models.Area;
import com.battlecity.models.Disposition;
import com.battlecity.models.PhysicalObject;
import com.battlecity.models.blocks.Tank;
import com.battlecity.server.controllers.GamesMgr;
import com.battlecity.utils.BytesUtils;
import com.battlecity.utils.CollusionUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MoveMessageHandler implements MessageHandler {

    private final GamesMgr gamesMgr;
    private final ScheduledExecutorService executorService;

    public MoveMessageHandler(GamesMgr gamesMgr, ScheduledExecutorService executorService) {
        this.gamesMgr = gamesMgr;
        this.executorService = executorService;
    }

    @Override
    public byte[] getType() {
        return MessageTypes.TYPE_MOVE;
    }

    @Override
    public void processMessage(Message message) throws MissingParamException {
        final long clientId = BytesUtils.byteArrayToLong(message.getRequiredProperty(MessageTypes.KEY_CONNECTION_ID));
        final Disposition disposition = Disposition.valueOf(BytesUtils.byteArrayToString(message.getRequiredProperty(MessageTypes.KEY_DISPOSITION)));
        gamesMgr.executeSynchronized(clientId, (game) -> {
            Tank tank = game.getTank(clientId);
            if (tank != null) {
                tank.cancelMoveTask(true);
                final AtomicInteger speed = new AtomicInteger(tank.getSpeed());
                tank.setMoveTask(executorService.scheduleAtFixedRate(() -> {
                    gamesMgr.executeSynchronized(clientId, (curGame) -> {
                        if (speed.get() == 0) {
                            tank.cancelMoveTask(false);
                            return;
                        }
                        Tank curTank = curGame.getTank(clientId);
                        if (curTank != null && curTank.getId() == tank.getId()) {
                            if (disposition == curTank.getDisposition()) {
                                Area areaAfterMove = curTank.getAreaAfterMove(disposition);
                                PhysicalObject physicalObject = curGame.getGameMap().getPhysicalObject(areaAfterMove, curTank.getId());
                                if (physicalObject == null && !CollusionUtils.checkCollusion(curGame.getGameMap(), areaAfterMove)) {
                                    curTank.move(disposition);
                                }
                            } else {
                                // only change orientation))
                                curTank.move(disposition);
                                tank.cancelMoveTask(false);
                            }
                        } else {
                            tank.cancelMoveTask(false);
                        }
                        speed.getAndDecrement();
                    });
                }, 0, 20, TimeUnit.MILLISECONDS));
            } else {
                // new live, spawn new tank
                game.tryToRespawnTank();
            }
        });
    }

}
