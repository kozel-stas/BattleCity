package com.battlecity.server.model;


import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.messages.Message;
import com.battlecity.models.*;
import com.battlecity.models.Iterable;
import com.battlecity.models.blocks.Fortress;
import com.battlecity.models.blocks.Tank;
import com.battlecity.server.controllers.MessageServer;
import com.battlecity.utils.CollusionUtils;
import com.battlecity.utils.IDGeneratorUtils;
import com.battlecity.utils.MapGeneratorUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Game implements Runnable, Comparable {

    private final long id;
    private final GameMap gameMap;
    private boolean completed = false;
    private final Map<Long, ClientConnection> clients;
    private final Lock lock = new ReentrantLock();
    private final MessageServer messageServer;

    public Game(ClientConnection clientConnection1, ClientConnection clientConnection2, MessageServer messageServer) {
        this.id = IDGeneratorUtils.generateID();
        this.clients = new TreeMap<>();
        this.clients.put(clientConnection1.getId(), clientConnection1);
        this.clients.put(clientConnection2.getId(), clientConnection2);
        this.gameMap = MapGeneratorUtils.generateMap(clientConnection1.getId(), clientConnection2.getId());
        this.messageServer = messageServer;
        tryToRespawnTank();
    }

    private void finish() {
        lock.lock();
        completed = true;
        Message message = new Message(MessageTypes.TYPE_FINISH);
        try {
            messageServer.sendMessageToAllConnection(message, clients.values());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        if (!completed) {
            lock.lock();
            for (Iterable iterable : gameMap.getIterableObjects().values()) {
                PhysicalObject physicalObject = gameMap.getPhysicalObject(iterable.getId());
                PhysicalObject physicalObjectConflict = gameMap.getPhysicalObject(iterable.getAreaAfterIterate(), iterable.getId());
                boolean collusionWithMap = CollusionUtils.checkCollusion(gameMap, iterable.getAreaAfterIterate());
                if (physicalObjectConflict == null && !collusionWithMap) {
                    iterable.doIterate();
                    continue;
                }
                processConflictPhysicalObject(physicalObjectConflict);
                if (physicalObject instanceof Destroyable) {
                    if (((Destroyable) physicalObject).destroyObject()) {
                        gameMap.removePhysicalObject(physicalObject.getId());
                    }
                } else {
                    // logic mistake
                    // remove object
                    gameMap.removePhysicalObject(physicalObject.getId());
                }
            }
            sendMapToClients();
            lock.unlock();
        }
    }

    public void processConflictPhysicalObject(PhysicalObject physicalObjectConflict) {
        if (physicalObjectConflict instanceof Destroyable) {
            if (((Destroyable) physicalObjectConflict).destroyObject()) {
                gameMap.removePhysicalObject(physicalObjectConflict.getId());
                if (physicalObjectConflict instanceof Tank) {
                    Tank tank = (Tank) physicalObjectConflict;
                    if (gameMap.removeTank(tank) != null) {
                        tryToRespawnTank();
                    }
                }
                if (physicalObjectConflict instanceof Fortress) {
                    Fortress fortress = (Fortress) physicalObjectConflict;
                    finish();
                }
            }
        }
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Game) {
            Game that = (Game) obj;
            return that.getId() == getId();
        }
        return false;
    }

    public Map<Long, ClientConnection> getClients() {
        return clients;
    }

    public boolean removeClientConnection(ClientConnection clientConnection) {
        if (containsClient(clientConnection)) {
            clients.remove(clientConnection.getId());
            return true;
        }
        return false;
    }

    public boolean containsClient(ClientConnection clientConnection) {
        return containsClient(clientConnection.getId());
    }

    public boolean containsClient(long id) {
        return clients.containsKey(id);
    }

    public void executeSynchronized(SynchronizeAction action) {
        lock.lock();
        try {
            action.execute(this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public Tank getTank(long clientId) {
        return gameMap.getTank(clientId);
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void tryToRespawnTank() {
        lock.lock();
        for (ClientConnection clientConnection : clients.values()) {
            gameMap.spawnTankForClient(clientConnection.getId());
        }
        lock.unlock();
    }

    private void sendMapToClients() {
        try {
            Message message = new Message(MessageTypes.TYPE_MAP);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(gameMap.getDrawableObjects());
            objectOutputStream.flush();
            // TODO fix me, need to send only updated object.
            message.setProperty(MessageTypes.KEY_GAME_MAP, byteArrayOutputStream.toByteArray());

            messageServer.sendMessageToAllConnection(message, clients.values());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return Long.compare(id, ((Game) o).id);
    }

}
