package com.battlecity.models.map;

import com.battlecity.models.blocks.Fortress;
import com.battlecity.models.blocks.Tank;
import com.battlecity.models.properties.*;
import com.battlecity.models.properties.Iterable;
import com.battlecity.utils.CollusionUtils;
import com.battlecity.utils.SpawnUtils;
import org.jetbrains.annotations.Nullable;

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

    private final ConcurrentSkipListMap<Long, Physical> physicalObjects = new ConcurrentSkipListMap<>();
    private final ConcurrentSkipListMap<Long, Movable> movableObjects = new ConcurrentSkipListMap<>();
    private final ConcurrentSkipListMap<Long, com.battlecity.models.properties.Iterable> iterableObjects = new ConcurrentSkipListMap<>();
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

    public boolean addPhysicalObjectToMap(Physical physical) {
        if (getPhysicalObject(physical.getId()) == null && getPhysicalObject(physical) == null &&
                !CollusionUtils.checkCollusion(this, physical.getPhysicalObject().getArea())) {
            physicalObjects.put(physical.getId(), physical);
            generifyPhysical(physical);
            return true;
        }
        return false;
    }

    @Nullable
    public Physical getPhysicalObject(Long id) {
        return physicalObjects.get(id);
    }

    @Nullable
    public Physical getPhysicalObject(Area area) {
        for (Physical physicalObject : physicalObjects.values()) {
            if (CollusionUtils.checkCollusion(physicalObject.getPhysicalObject(), area)) {
                return physicalObject;
            }
        }
        return null;
    }

    @Nullable
    public Physical getPhysicalObject(Area area, long id) {
        for (Physical physicalObject : physicalObjects.values()) {
            if (CollusionUtils.checkCollusion(physicalObject.getPhysicalObject(), area) && physicalObject.getId() != id) {
                return physicalObject;
            }
        }
        return null;
    }

    @Nullable
    public Physical getPhysicalObject(Physical physical) {
        for (Physical currPhysicalObject : physicalObjects.values()) {
            if (CollusionUtils.checkCollusion(currPhysicalObject.getPhysicalObject(), physical.getPhysicalObject())) {
                return currPhysicalObject;
            }
        }
        return null;
    }

    @Nullable
    public Physical removePhysical(Long id) {
        Physical physical = physicalObjects.remove(id);
        if (physical != null) {
            movableObjects.remove(physical.getId());
            destroyableObjects.remove(physical.getId());
            iterableObjects.remove(physical.getId());
            drawableObjects.remove(physical.getId());
            return physical;
        }
        return null;
    }

    @Nullable
    public Physical removePhysical(Physical physical) {
        return removePhysical(physical.getId());
    }

    private void generifyPhysical(Physical physical) {
        if (physical instanceof Movable) {
            movableObjects.put(physical.getId(), (Movable) physical);
        }
        if (physical instanceof Destroyable) {
            destroyableObjects.put(physical.getId(), (Destroyable) physical);
        }
        if (physical instanceof com.battlecity.models.properties.Iterable) {
            iterableObjects.put(physical.getId(), (com.battlecity.models.properties.Iterable) physical);
        }
        if (physical instanceof Drawable) {
            drawableObjects.put(physical.getId(), (Drawable) physical);
        }
    }

    public ConcurrentSkipListMap<Long, Iterable> getIterableObjects() {
        return iterableObjects;
    }

    public ConcurrentSkipListMap<Long, Physical> getPhysicalObjects() {
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
