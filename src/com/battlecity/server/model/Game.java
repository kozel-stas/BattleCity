package com.battlecity.server.model;


import com.battlecity.models.*;
import com.battlecity.models.Iterable;
import com.battlecity.models.blocks.Tank;
import com.battlecity.utils.IDGeneratorUtil;
import com.battlecity.utils.MapGeneratorUtil;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Game implements Runnable {

    private long id;

    private GameMap gameMap;

    private boolean completed = false;

    private Map<Long, ClientConnection> clients;
    // clientId -> Tank
    private Map<Long, Tank> tanks;

    private final Lock lock = new ReentrantLock();

    public Game(ClientConnection clientConnection1, ClientConnection clientConnection2) {
        this.id = IDGeneratorUtil.generate();
        this.clients = new TreeMap<>();
        this.clients.put(clientConnection1.getId(), clientConnection1);
        this.clients.put(clientConnection2.getId(), clientConnection2);
        this.gameMap = MapGeneratorUtil.generateMap();
        tryToRespawnTank();
    }

    public void finish() {
        completed = true;
    }

    @Override
    public void run() {
        if (!completed) {
            for (Iterable iterable : gameMap.getIterableObjects().values()) {
                PhysicalObject physicalObject = gameMap.getPhysicalObject(iterable.getId());
                PhysicalObject physicalObjectConflict = gameMap.getPhysicalObject(iterable.getAreaAfterIterate());
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
                    if (((Destroyable) physicalObjectConflict).destroyObject()) {
                        gameMap.removePhysicalObject(physicalObjectConflict.getId());
                    }
                } else {
                    // logic mistake
                    // remove object
                    gameMap.removePhysicalObject(physicalObject.getId());
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

    public PhysicalObject getPhysicalObject(Area area) {
        return gameMap.getPhysicalObject(area);
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
                gameMap.addPhysicalObjectToMap(tank);
                //publish new event
                return tank;
            });
        }
    }

}
