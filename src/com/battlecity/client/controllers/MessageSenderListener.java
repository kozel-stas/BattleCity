package com.battlecity.client.controllers;

import com.battlecity.communication.MessageRouter;
import com.battlecity.communication.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageSenderListener implements Runnable {

    public static final int PORT = 3030;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final MessageRouter messageRouter;

    MessageSenderListener(MessageRouter messageRouter) {
        this.messageRouter = messageRouter;
    }

    private synchronized void tryConnect() throws IOException {
        if (isReady()) {
            return;
        }
        socket = new Socket("206.189.97.184", PORT);

        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        executorService.scheduleAtFixedRate(this, 500, 50, TimeUnit.MICROSECONDS);
    }

    public synchronized void sendMessage(Message message) throws IOException {
        if (isReady()) {
            objectOutputStream.reset();
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } else {
            tryConnect();
            sendMessage(message);
        }
    }

    private boolean isReady() {
        return !(socket == null || objectInputStream == null || objectOutputStream == null);
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (isReady()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.socket = null;
        this.objectInputStream = null;
        this.objectOutputStream = null;
    }

    @Override
    public void run() {
        if (isReady()) try {
            if (socket.getInputStream().available() > 0) {
                Object object = objectInputStream.readObject();

                if (object instanceof Message) {
                    Message message = (Message) object;
                    messageRouter.processMessage(message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
