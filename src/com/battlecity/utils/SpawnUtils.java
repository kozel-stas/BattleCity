package com.battlecity.utils;

import com.battlecity.models.Area;
import com.battlecity.models.GameMap;
import com.battlecity.models.GameProperties;
import com.battlecity.models.blocks.Fortress;

public class SpawnUtils {

    public static Area findPlaceForTank(GameProperties gameProperties, GameMap map, Fortress fortress) {
        for (int i = fortress.getCoordinateX(); i < gameProperties.getMapArea().getWidth(); i += 10) {
            for (int j = fortress.getCoordinateY(); j < gameProperties.getMapArea().getHeight(); j += 10) {
                Area area = new Area(i, j, gameProperties.getTankArea().getHeight(), gameProperties.getTankArea().getWidth());
                if (map.getPhysicalObject(area) == null) {
                    return area;
                }
            }
        }
        for (int i = fortress.getCoordinateX(); i >= 0; i -= 10) {
            for (int j = fortress.getCoordinateY(); j >= 0; j -= 10) {
                Area area = new Area(i, j, gameProperties.getTankArea().getHeight(), gameProperties.getTankArea().getWidth());
                if (map.getPhysicalObject(area) == null) {
                    return area;
                }
            }
        }
        return null;
    }

}
