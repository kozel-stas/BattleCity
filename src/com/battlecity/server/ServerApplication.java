package com.battlecity.server;

import com.battlecity.communication.MessageRouter;
import com.battlecity.communication.handlers.MoveMessageHandler;
import com.battlecity.communication.handlers.ShootMessageHandler;
import com.battlecity.models.Area;
import com.battlecity.models.GameMap;
import com.battlecity.models.MapSize;
import com.battlecity.models.PhysicalObject;
import com.battlecity.server.controllers.GamesMgr;
import com.battlecity.server.controllers.MessageServer;

public class ServerApplication {

    private MessageRouter messageRouter;
    private GamesMgr gamesMgr;
    private MessageServer messageServer;

    public ServerApplication() {
        messageRouter = new MessageRouter();
        gamesMgr = new GamesMgr();
        messageRouter.addMessageHandler(new MoveMessageHandler(gamesMgr, messageServer));
        messageRouter.addMessageHandler(new ShootMessageHandler(gamesMgr, messageServer));
        messageServer = new MessageServer(messageRouter, gamesMgr);
    }

    public void startApp() {
        messageServer.run();
    }

    public static void main(String[] args) {

        PhysicalObject physicalObject = new PhysicalObject(0, 0, 2, 2);

        GameMap gameMap = new GameMap(MapSize.STANDART);

        System.out.println(gameMap.addPhysicalObjectToMap(physicalObject));

        System.out.println(gameMap.getPhysicalObject(new Area(1, 1, 1, 1)));

        new ServerApplication().startApp();

    }
}
