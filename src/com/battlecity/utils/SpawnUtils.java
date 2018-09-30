package com.battlecity.utils;

import com.battlecity.models.blocks.Fortress;
import com.battlecity.models.map.Area;
import com.battlecity.models.map.GameMap;
import com.battlecity.models.properties.GameProperties;

public class SpawnUtils {

    public static Area findPlaceForTank(GameProperties gameProperties, GameMap map, Fortress fortress) {
        for (int i = fortress.getPhysicalObject().getCoordinateX(); i < gameProperties.getMapArea().getWidth(); i += 10) {
            for (int j = fortress.getPhysicalObject().getCoordinateY(); j < gameProperties.getMapArea().getHeight(); j += 10) {
                Area area = new Area(i, j, gameProperties.getTankArea().getHeight(), gameProperties.getTankArea().getWidth());
                if (map.getPhysicalObject(area) == null) {
                    return area;
                }
            }
        }
        for (int i = fortress.getPhysicalObject().getCoordinateX(); i >= 0; i -= 10) {
            for (int j = fortress.getPhysicalObject().getCoordinateY(); j >= 0; j -= 10) {
                Area area = new Area(i, j, gameProperties.getTankArea().getHeight(), gameProperties.getTankArea().getWidth());
                if (map.getPhysicalObject(area) == null) {
                    return area;
                }
            }
        }
        return null;
    }

}
