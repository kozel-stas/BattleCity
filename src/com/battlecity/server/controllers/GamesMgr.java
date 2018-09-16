package com.battlecity.server.controllers;

import com.battlecity.server.model.ClientConnection;
import com.battlecity.server.model.Game;

import java.util.concurrent.ConcurrentSkipListSet;

public class GamesMgr {

    private final ConcurrentSkipListSet<Game> games = new ConcurrentSkipListSet<>();
    private final ConcurrentSkipListSet<ClientConnection> waitingConnections = new ConcurrentSkipListSet<>();

    public void addConnection(ClientConnection clientConnection) {
        waitingConnections.add(clientConnection);
        findGame();
    }

    public synchronized void removeConnection(ClientConnection clientConnection) {
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

    private synchronized void findGame() {
        synchronized (waitingConnections) {
            while (waitingConnections.size() > 1) {
                ClientConnection clientConnection1 = waitingConnections.pollFirst();
                ClientConnection clientConnection2 = waitingConnections.pollFirst();
                if (clientConnection1 == null || clientConnection2 == null) {
                    // impossible case
                    continue;
                }
                games.add(new Game(clientConnection1, clientConnection2));
            }
        }
    }

    public void executeSynchronized(long id, SynchronizeAction synchronizeAction) {
        for (Game game : games) {
            if (game.containsClient(id)) {
                game.getLock().lock();
                try {
                    synchronizeAction.execute(game);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    game.getLock().unlock();
                }
            }
        }
    }

    public interface SynchronizeAction {

        public void execute(Game game);

    }

}
