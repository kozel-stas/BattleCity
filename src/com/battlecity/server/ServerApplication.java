package com.battlecity.server;

import com.battlecity.communication.MessageRouter;
import com.battlecity.communication.handlers.MoveMessageHandler;
import com.battlecity.communication.handlers.ShootMessageHandler;
import com.battlecity.server.controllers.GamesMgr;
import com.battlecity.server.controllers.MessageServer;

import java.util.HashMap;
import java.util.Map;

public class ServerApplication {

    private MessageRouter messageRouter;
    private GamesMgr gamesMgr;
    private MessageServer messageServer;

    public ServerApplication() {
        messageRouter = new MessageRouter();
        gamesMgr = new GamesMgr();
        messageRouter.addMessageHandler(new MoveMessageHandler(gamesMgr));
        messageRouter.addMessageHandler(new ShootMessageHandler(gamesMgr, messageServer));
        messageServer = new MessageServer(messageRouter, gamesMgr);
        gamesMgr.setMessageServer(messageServer);//TODO fix
    }

    public void startApp() {
        messageServer.run();
    }

    public static void main(String[] args) {

        Map<Long, String> a =new HashMap<>();


        a.computeIfAbsent(1L, id ->{
            return "cerf";
        });

        System.out.println(a.values());

        new ServerApplication().startApp();

    }
}
