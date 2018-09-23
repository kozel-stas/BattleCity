package com.battlecity.client.controllers;

import com.battlecity.client.view.GameWindow;
import com.battlecity.communication.MessageRouter;
import com.battlecity.communication.handlers.AvailabilityMessageHandler;
import com.battlecity.communication.handlers.FinishGameMessageHandler;
import com.battlecity.communication.handlers.MapMessageHandler;

public class ClientApplication {

    private final MessageRouter messageRouter;
    private final MessageSenderListener messageSender;

    public ClientApplication(GameWindow gameWindow) {
        messageRouter = new MessageRouter();
        messageRouter.addMessageHandler(new AvailabilityMessageHandler());
        messageRouter.addMessageHandler(new MapMessageHandler(gameWindow));
        messageRouter.addMessageHandler(new FinishGameMessageHandler(gameWindow));
        messageSender = new MessageSenderListener(messageRouter);
    }

    public MessageSenderListener getMessageSender() {
        return messageSender;
    }

}
