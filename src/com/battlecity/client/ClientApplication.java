package com.battlecity.client;

import com.battlecity.client.controllers.MessageSender;

public class ClientApplication {

    private final MessageSender messageSender;

    public ClientApplication(){
        messageSender = new MessageSender();
    }

    public void startApp(){
        messageSender.tryConnect();
    }

    public static void main(String[] args) {

      new ClientApplication().startApp();

    }

}
