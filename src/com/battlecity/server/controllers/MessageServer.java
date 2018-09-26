package com.battlecity.server.controllers;

import com.battlecity.communication.MessageRouter;
import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.messages.Message;
import com.battlecity.server.model.ClientConnection;
import com.battlecity.utils.BytesUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageServer implements Runnable {

    private static final int PORT = 3030;
    private static final Logger LOG = Logger.getLogger(MessageServer.class.getName());
    private static final int NUMBER_OF_THREADS = 4;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(NUMBER_OF_THREADS);
    private final ConcurrentSkipListSet<ClientConnection> connections = new ConcurrentSkipListSet<>();
    private final MessageRouter messageRouter;
    private final GamesMgr gamesMgr;

    private volatile boolean shutdown = false;

    public MessageServer(MessageRouter messageRouter, GamesMgr gamesMgr) {
        this.messageRouter = messageRouter;
        this.gamesMgr = gamesMgr;
    }

    private synchronized void sendMessageToConnection(Message message, ClientConnection clientConnection) throws IOException {
        clientConnection.getObjectOutputStream().writeObject(message);
        clientConnection.getObjectOutputStream().reset();
    }

    public void sendMessageToAllConnection(Message message, Collection<ClientConnection> clientConnections) throws IOException {
        for (ClientConnection clientConnection : clientConnections) {
            sendMessageToConnection(message, clientConnection);
        }
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            executor.scheduleAtFixedRate(new AvailabilityTask(), 500, 5000, TimeUnit.MILLISECONDS);
            executor.scheduleAtFixedRate(new ReadTask(), 500, 50, TimeUnit.MICROSECONDS);

            while (isRunning()) {
                Socket socket = serverSocket.accept();

                ClientConnection clientConnection = new ClientConnection(socket);

                connections.add(clientConnection);
                gamesMgr.addConnection(clientConnection);
            }
        } catch (IOException e) {
            LOG.log(Level.OFF, "Fatal error", e);
        }
    }

    private boolean isRunning() {
        return !shutdown;
    }

    public void shutdown() {
        this.shutdown = true;
        executor.shutdown();
    }

    private class ReadTask implements Runnable {

        @Override
        public void run() {
            if (isRunning()) {
                for (ClientConnection clientConnection : connections) {
                    try {
                        if (clientConnection.getSocket().getInputStream().available() <= 0) {
                            continue;
                        }

                        Object object = clientConnection.getObjectInputStream().readObject();

                        if (object instanceof Message) {
                            Message message = (Message) object;
                            message.setProperty(MessageTypes.KEY_CONNECTION_ID, BytesUtils.toByteArray(clientConnection.getId()));

                            executor.execute(() -> {
                                messageRouter.processMessage(message);
                            });
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        LOG.log(Level.WARNING, "Exception was thrown", e);
                    }
                }
            }
        }
    }

    private class AvailabilityTask implements Runnable {

        @Override
        public void run() {
            if (isRunning()) {
                Iterator<ClientConnection> iterator = connections.iterator();
                while (iterator.hasNext()) {
                    ClientConnection clientConnection = iterator.next();
                    try {
                        if (clientConnection.getSocket().isClosed()) {
                            iterator.remove();
                            gamesMgr.removeConnection(clientConnection);
                            continue;
                        }
                        sendMessageToConnection(new Message(MessageTypes.TYPE_AVAILABILITY), clientConnection);
                    } catch (IOException e) {
                        e.printStackTrace();
                        iterator.remove();
                        gamesMgr.removeConnection(clientConnection);
                        continue;
                    }
                }
            }
        }

    }

}
