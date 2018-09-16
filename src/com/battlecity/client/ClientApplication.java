package com.battlecity.client;

import com.battlecity.client.controllers.MessageSenderListener;
import com.battlecity.communication.MessageRouter;
import com.battlecity.communication.handlers.AvailabilityMessageHandler;

import java.io.IOException;

public class ClientApplication {

    private final MessageRouter messageRouter;
    private final MessageSenderListener messageSender;

    public ClientApplication() {
        messageRouter = new MessageRouter();
        messageRouter.addMessageHandler(new AvailabilityMessageHandler());
        messageSender = new MessageSenderListener(messageRouter);
    }

    public void startApp() throws IOException {
        messageSender.tryConnect();
    }

    public static void main(String[] args) {
        try {
            new ClientApplication().startApp();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
