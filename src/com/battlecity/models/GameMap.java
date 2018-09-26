package com.battlecity.models;

import com.battlecity.models.blocks.Fortress;
import com.battlecity.models.blocks.Tank;
import com.battlecity.utils.CollusionUtils;
import com.battlecity.utils.SpawnUtils;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * ----------------------------------------------------->
 * |1 1
 * |1 1
 * |    0 0
 * |    0 0
 * |
 * |
 * |
 * |
 * |
 * |
 * |
 * |
 * |
 * |
 * |
 * \/
 */
public class GameMap {

    private final GameProperties gameProperties;

    private final ConcurrentSkipListMap<Long, PhysicalObject> physicalObjects = new ConcurrentSkipListMap<>();
    private final ConcurrentSkipListMap<Long, Movable> movableObjects = new ConcurrentSkipListMap<>();
    private final ConcurrentSkipListMap<Long, Iterable> iterableObjects = new ConcurrentSkipListMap<>();
    private final ConcurrentSkipListMap<Long, Destroyable> destroyableObjects = new ConcurrentSkipListMap<>();
    private final ConcurrentSkipListMap<Long, Drawable> drawableObjects = new ConcurrentSkipListMap<>();
    // clientId -> Tank
    private final ConcurrentSkipListMap<Long, Tank> tanks = new ConcurrentSkipListMap<>();
    // clientId -> Fortress
    private final ConcurrentSkipListMap<Long, Fortress> fortresses = new ConcurrentSkipListMap<>();

    public GameMap(GameProperties gameProperties, Fortress fortress1, Fortress fortress2) {
        this.gameProperties = gameProperties;
        fortresses.put(fortress1.getOwner(), fortress1);
        fortresses.put(fortress2.getOwner(), fortress2);
        addPhysicalObjectToMap(fortress1);
        addPhysicalObjectToMap(fortress2);
    }

    public boolean addPhysicalObjectToMap(PhysicalObject physicalObject) {
        if (getPhysicalObject(physicalObject.getId()) == null && getPhysicalObject(physicalObject) == null && !CollusionUtils.checkCollusion(this, physicalObject.getArea())) {
            physicalObjects.put(physicalObject.getId(), physicalObject);
            generifyPhysicalObjects(physicalObject);
            return true;
        }
        return false;
    }

    @Nullable
    public PhysicalObject getPhysicalObject(Long id) {
        return physicalObjects.get(id);
    }

    @Nullable
    public PhysicalObject getPhysicalObject(Area area) {
        for (PhysicalObject physicalObject : physicalObjects.values()) {
            if (CollusionUtils.checkCollusion(physicalObject, area)) {
                return physicalObject;
            }
        }
        return null;
    }

    @Nullable
    public PhysicalObject getPhysicalObject(Area area, long id) {
        for (PhysicalObject physicalObject : physicalObjects.values()) {
            if (CollusionUtils.checkCollusion(physicalObject, area) && physicalObject.getId() != id) {
                return physicalObject;
            }
        }
        return null;
    }

    @Nullable
    public PhysicalObject getPhysicalObject(PhysicalObject physicalObject) {
        for (PhysicalObject currPhysicalObject : physicalObjects.values()) {
            if (CollusionUtils.checkCollusion(currPhysicalObject, physicalObject)) {
                return currPhysicalObject;
            }
        }
        return null;
    }

    @Nullable
    public PhysicalObject removePhysicalObject(Long id) {
        PhysicalObject physicalObject = physicalObjects.remove(id);
        if (physicalObject != null) {
            movableObjects.remove(physicalObject.getId());
            destroyableObjects.remove(physicalObject.getId());
            iterableObjects.remove(physicalObject.getId());
            drawableObjects.remove(physicalObject.getId());
            return physicalObject;
        }
        return null;
    }

    @Nullable
    public PhysicalObject removePhysicalObject(PhysicalObject physicalObject) {
        return removePhysicalObject(physicalObject.getId());
    }

    private void generifyPhysicalObjects(PhysicalObject physicalObject) {
        if (physicalObject instanceof Movable) {
            movableObjects.put(physicalObject.getId(), (Movable) physicalObject);
        }
        if (physicalObject instanceof Destroyable) {
            destroyableObjects.put(physicalObject.getId(), (Destroyable) physicalObject);
        }
        if (physicalObject instanceof Iterable) {
            iterableObjects.put(physicalObject.getId(), (Iterable) physicalObject);
        }
        if (physicalObject instanceof Drawable) {
            drawableObjects.put(physicalObject.getId(), (Drawable) physicalObject);
        }
    }

    public ConcurrentSkipListMap<Long, Iterable> getIterableObjects() {
        return iterableObjects;
    }

    public ConcurrentSkipListMap<Long, PhysicalObject> getPhysicalObjects() {
        return physicalObjects;
    }

    public GameProperties getGameProperties() {
        return gameProperties;
    }

    public boolean spawnTankForClient(Long clientID) {
        Tank tank = tanks.get(clientID);
        if (tank == null) {
            Area area = SpawnUtils.findPlaceForTank(gameProperties, this, fortresses.get(clientID));
            if (area == null) {
                return false;
            }
            tank = new Tank(area.getCoordinateX(), area.getCoordinateY(), gameProperties);
            if (addPhysicalObjectToMap(tank)) {
                tanks.put(clientID, tank);
                return true;
            }
        }
        return false;
    }

    public Tank getTank(long clientID) {
        return tanks.get(clientID);
    }

    public Tank removeTank(long clientID) {
        return tanks.remove(clientID);
    }

    public Tank removeTank(Tank tank) {
        for (Map.Entry<Long, Tank> entry : tanks.entrySet()) {
            if (tank.equals(entry.getValue())) {
                return removeTank(entry.getKey());
            }
        }
        return null;
    }

    public ConcurrentSkipListMap<Long, Drawable> getDrawableObjects() {
        return drawableObjects;
    }

}
