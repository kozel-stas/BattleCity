package com.battlecity.server.controllers;

import com.battlecity.server.model.ClientConnection;
import com.battlecity.server.model.Game;
import com.battlecity.server.model.SynchronizeAction;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class GamesMgr {

    private final ConcurrentSkipListSet<Game> games = new ConcurrentSkipListSet<>();
    private final ConcurrentSkipListSet<ClientConnection> waitingConnections = new ConcurrentSkipListSet<>();
    private final ScheduledExecutorService executorService;

    public GamesMgr(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    private MessageServer messageServer;

    void addConnection(ClientConnection clientConnection) {
        waitingConnections.add(clientConnection);
        tryToFindGame();
    }

    synchronized void removeConnection(ClientConnection clientConnection) {
        synchronized (waitingConnections) {
            if (waitingConnections.remove(clientConnection)) {
                return;
            }
        }
        for (Game game : games) {
            if (game.removeClientConnection(clientConnection)) {
                waitingConnections.addAll(game.getClients().values());
                games.remove(game);
                return;
            }
        }
    }

    private synchronized void tryToFindGame() {
        synchronized (waitingConnections) {
            while (waitingConnections.size() > 1) {
                ClientConnection clientConnection1 = waitingConnections.pollFirst();
                ClientConnection clientConnection2 = waitingConnections.pollFirst();
                Game game = new Game(clientConnection1, clientConnection2, messageServer);
                executorService.scheduleAtFixedRate(game, 10, 50, TimeUnit.MILLISECONDS);
                games.add(game);
            }
        }
    }

    public void executeSynchronized(long id, SynchronizeAction synchronizeAction) {
        for (Game game : games) {
            if (game.containsClient(id)) {
                game.executeSynchronized(synchronizeAction);
                return;
            }
        }
    }

    public void setMessageServer(MessageServer messageServer) {
        this.messageServer = messageServer;
    }
}
