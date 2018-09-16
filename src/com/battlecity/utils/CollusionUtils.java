package com.battlecity.utils;

import com.battlecity.models.Area;
import com.battlecity.models.GameMap;
import com.battlecity.models.PhysicalObject;

/**
 * Utility class for checking collusion on map.
 */
public class CollusionUtils {

    public static boolean checkCollusion(Area area1, Area area2) {
        return ((area1.getCoordinateY() < area2.getCoordinateY() && area2.getCoordinateY() < area1.getCoordinateY1()) ||
                (area1.getCoordinateY() < area2.getCoordinateY1() && area2.getCoordinateY1() < area1.getCoordinateY1())) &&
                ((area1.getCoordinateX() < area2.getCoordinateX() && area2.getCoordinateX() < area1.getCoordinateX1()) ||
                        (area1.getCoordinateX() < area2.getCoordinateX1() && area2.getCoordinateX1() < area1.getCoordinateX1()));
    }

    public static boolean checkCollusion(Area area, PhysicalObject physicalObject) {
        return ((area.getCoordinateY() < physicalObject.getCoordinateY() && physicalObject.getCoordinateY() < area.getCoordinateY1()) ||
                (area.getCoordinateY() < physicalObject.getCoordinateY1() && physicalObject.getCoordinateY1() < area.getCoordinateY1())) &&
                ((area.getCoordinateX() < physicalObject.getCoordinateX() && physicalObject.getCoordinateX() < area.getCoordinateX1()) ||
                        (area.getCoordinateX() < physicalObject.getCoordinateX1() && physicalObject.getCoordinateX1() < area.getCoordinateX1()));
    }

    public static boolean checkCollusion(PhysicalObject physicalObject, Area area) {
        return ((physicalObject.getCoordinateY() < area.getCoordinateY() && area.getCoordinateY() < physicalObject.getCoordinateY1()) ||
                (physicalObject.getCoordinateY() < area.getCoordinateY1() && area.getCoordinateY1() < physicalObject.getCoordinateY1())) &&
                ((physicalObject.getCoordinateX() < area.getCoordinateX() && area.getCoordinateX() < physicalObject.getCoordinateX1()) ||
                        (physicalObject.getCoordinateX() < area.getCoordinateX1() && area.getCoordinateX1() < physicalObject.getCoordinateX1()));
    }

    public static boolean checkCollusion(PhysicalObject physicalObject1, PhysicalObject physicalObject2) {
        return ((physicalObject1.getCoordinateY() < physicalObject2.getCoordinateY() && physicalObject2.getCoordinateY() < physicalObject1.getCoordinateY1()) ||
                (physicalObject1.getCoordinateY() < physicalObject2.getCoordinateY1() && physicalObject2.getCoordinateY1() < physicalObject1.getCoordinateY1())) &&
                ((physicalObject1.getCoordinateX() < physicalObject2.getCoordinateX() && physicalObject2.getCoordinateX() < physicalObject1.getCoordinateX1()) ||
                        (physicalObject1.getCoordinateX() < physicalObject2.getCoordinateX1() && physicalObject2.getCoordinateX1() < physicalObject1.getCoordinateX1()));
    }

    public static boolean checkCollusion(GameMap gameMap, Area area) {
        Area mapArea = gameMap.getMapSize().getMapArea();
        return !(0 < area.getCoordinateX() && mapArea.getCoordinateX1() > area.getCoordinateX1()
                && 0 < area.getCoordinateY() && mapArea.getCoordinateY1() > area.getCoordinateY1());
    }
}
