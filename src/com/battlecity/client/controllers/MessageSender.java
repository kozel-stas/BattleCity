package com.battlecity.client.controllers;

import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.messages.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageSender {

    private Socket socket;

    public void tryConnect() {
        try {
            socket = new Socket("localhost", 1234);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            objectOutputStream.writeObject(new Message(MessageTypes.TYPE_MOVE));
            objectOutputStream.flush();

            while (true) ;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessage(Message message) {

    }


}
