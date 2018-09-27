package com.battlecity.server;

import com.battlecity.communication.MessageRouter;
import com.battlecity.communication.handlers.MoveMessageHandler;
import com.battlecity.communication.handlers.ShootMessageHandler;
import com.battlecity.server.controllers.GamesMgr;
import com.battlecity.server.controllers.MessageServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ServerApplication {

    private static final int NUMBER_OF_THREADS = 15;

    private MessageRouter messageRouter;
    private GamesMgr gamesMgr;
    private MessageServer messageServer;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(NUMBER_OF_THREADS);

    private ServerApplication() {
        messageRouter = new MessageRouter();
        gamesMgr = new GamesMgr(executor);
        messageRouter.addMessageHandler(new MoveMessageHandler(gamesMgr, executor));
        messageRouter.addMessageHandler(new ShootMessageHandler(gamesMgr));
        messageServer = new MessageServer(messageRouter, gamesMgr, executor);
        gamesMgr.setMessageServer(messageServer);
    }

    private void startApp() {
        messageServer.run();
    }

    public static void main(String[] args) {
        new ServerApplication().startApp();
    }
}
