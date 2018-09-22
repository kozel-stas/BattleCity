package com.battlecity.models;

import com.battlecity.utils.CollusionUtils;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
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
public class GameMap implements Serializable {

    private final MapSize mapSize;

    private final ConcurrentSkipListMap<Long, PhysicalObject> physicalObjects = new ConcurrentSkipListMap<>();
    private final ConcurrentSkipListMap<Long, Movable> movableObjects = new ConcurrentSkipListMap<>();
    private final ConcurrentSkipListMap<Long, Iterable> iterableObjects = new ConcurrentSkipListMap<>();
    private final ConcurrentSkipListMap<Long, Destroyable> destroyableObjects = new ConcurrentSkipListMap<>();

    public GameMap(MapSize mapSize) {
        this.mapSize = mapSize;
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
                return physicalObject;
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
    }

    public ConcurrentSkipListMap<Long, Iterable> getIterableObjects() {
        return iterableObjects;
    }

    public ConcurrentSkipListMap<Long, PhysicalObject> getPhysicalObjects() {
        return physicalObjects;
    }

    public MapSize getMapSize() {
        return mapSize;
    }
}
