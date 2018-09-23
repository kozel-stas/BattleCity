package com.battlecity.server.model;


import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.messages.Message;
import com.battlecity.models.*;
import com.battlecity.models.Iterable;
import com.battlecity.models.blocks.Tank;
import com.battlecity.server.controllers.MessageServer;
import com.battlecity.utils.IDGeneratorUtil;
import com.battlecity.utils.MapGeneratorUtil;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Game implements Runnable, Comparable {

    private long id;

    private GameMap gameMap;

    private boolean completed = false;

    private Map<Long, ClientConnection> clients;

    // clientId -> Tank
    private Map<Long, Tank> tanks = new HashMap<>();

    private final Lock lock = new ReentrantLock();

    private final MessageServer messageServer;

    public Game(ClientConnection clientConnection1, ClientConnection clientConnection2, MessageServer messageServer) {
        System.out.println("new Game");
        this.id = IDGeneratorUtil.generate();
        this.clients = new TreeMap<>();
        this.clients.put(clientConnection1.getId(), clientConnection1);
        this.clients.put(clientConnection2.getId(), clientConnection2);
        this.gameMap = MapGeneratorUtil.generateMap();
        this.messageServer = messageServer;
        tryToRespawnTank();
    }

    public void finish() {
        completed = true;
    }

    @Override
    public void run() {
        if (!completed) {
            lock.lock();
            for (Iterable iterable : gameMap.getIterableObjects().values()) {
                PhysicalObject physicalObject = gameMap.getPhysicalObject(iterable.getId());
                PhysicalObject physicalObjectConflict = gameMap.getPhysicalObject(iterable.getAreaAfterIterate(), iterable.getId());
                if (physicalObjectConflict == null) {
                    iterable.doIterate();
                    continue;
                }
                if (physicalObjectConflict instanceof Destroyable) {
                    if (((Destroyable) physicalObjectConflict).destroyObject()) {
                        gameMap.removePhysicalObject(physicalObjectConflict.getId());
                    }
                    if (physicalObjectConflict instanceof Tank) {
                        Tank tank = (Tank) physicalObjectConflict;
                        if (removeTank(tank)) {
                            tryToRespawnTank();
                        }
                    }
                }
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
            finish();
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

    public Lock getLock() {
        return lock;
    }

    public Tank getTank(long clientId) {
        return tanks.get(clientId);
    }

    public PhysicalObject getPhysicalObject(Area area, long id) {
        return gameMap.getPhysicalObject(area, id);
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public boolean removeTank(Tank tank) {
        for (Map.Entry<Long, Tank> entry : tanks.entrySet()) {
            if (tank.equals(entry.getValue())) {
                tanks.remove(entry.getKey());
                return true;
            }
        }
        return false;
    }

    public synchronized void tryToRespawnTank() {
        for (ClientConnection clientConnection : clients.values()) {
            tanks.computeIfAbsent(clientConnection.getId(), (id) -> {
                Tank tank = new Tank(50, 50, gameMap.getMapSize());
                System.out.println(gameMap.addPhysicalObjectToMap(tank));
                return tank;
            });
        }
        sendMapToClients();
    }

    public void sendMapToClients() {
        try {
            Message message = new Message(MessageTypes.TYPE_MAP);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(gameMap);
            objectOutputStream.flush();
            message.setProperty(MessageTypes.KEY_GAME_MAP, byteArrayOutputStream.toByteArray());

            messageServer.sendMessageToAllConnection(message, clients.values());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return (int) Long.compare(id, ((Game) o).id);
    }
}
