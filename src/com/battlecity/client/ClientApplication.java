package com.battlecity.client;

import com.battlecity.client.controllers.MessageSenderListener;
import com.battlecity.client.view.MainWindow;
import com.battlecity.communication.MessageRouter;
import com.battlecity.communication.handlers.AvailabilityMessageHandler;
import com.battlecity.communication.handlers.MapMessageHandler;

import java.io.IOException;

public class ClientApplication {

    private final MessageRouter messageRouter;
    private final MessageSenderListener messageSender;

    public ClientApplication() {
        messageRouter = new MessageRouter();
        messageRouter.addMessageHandler(new AvailabilityMessageHandler());
        messageRouter.addMessageHandler(new MapMessageHandler());
        messageSender = new MessageSenderListener(messageRouter);
    }

    public void startApp() throws IOException {
        messageSender.tryConnect();
    }

    public static void main(String[] args) {
        ClientApplication clientApplication = new ClientApplication();
        new Thread(()->{
            try {
                clientApplication.startApp();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        new MainWindow(clientApplication);
    }

    public MessageSenderListener getMessageSender() {
        return messageSender;
    }

}
