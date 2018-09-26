package com.battlecity.utils;

import com.battlecity.models.GameMap;
import com.battlecity.models.MapSize;
import com.battlecity.models.PhysicalObject;
import com.battlecity.models.blocks.Block;
import com.battlecity.models.blocks.Fortress;

import java.util.Random;

public class MapGeneratorUtil {

    public static GameMap generateMap(long clientID1, long clientID2) {
        Fortress[] fortresses = getAreaForFortresses(MapSize.STANDART, clientID1, clientID2);
        GameMap gameMap = new GameMap(MapSize.STANDART, fortresses[0], fortresses[1]);
        PhysicalObject[] physicalObjects1 = generateWallAroundFortress(MapSize.STANDART, fortresses[0]);
        PhysicalObject[] physicalObjects2 = generateWallAroundFortress(MapSize.STANDART, fortresses[1]);
        addPhysicalObjectsToGameMapIfItPossible(gameMap, physicalObjects1);
        addPhysicalObjectsToGameMapIfItPossible(gameMap, physicalObjects2);
        return gameMap;
    }

    private static Fortress[] getAreaForFortresses(MapSize mapSize, long clientID1, long clientID2) {
        Random random = new Random();
        if (random.nextBoolean()) {
            int end = (int) mapSize.getMapArea().getWidth() / mapSize.getBlockArea().getWidth();
            int abstractPosition = random.nextInt(end);
            Fortress fortress1 = new Fortress(mapSize.getBlockArea().getWidth() * abstractPosition, 0, mapSize, clientID1);
            Fortress fortress2 = new Fortress(mapSize.getBlockArea().getWidth() * abstractPosition, mapSize.getMapArea().getHeight() - mapSize.getBlockArea().getHeight(), mapSize, clientID2);
            return new Fortress[]{fortress1, fortress2};
        } else {
            int end = (int) mapSize.getMapArea().getHeight() / mapSize.getBlockArea().getHeight();
            int abstractPosition = random.nextInt(end);
            Fortress fortress1 = new Fortress(0, mapSize.getBlockArea().getHeight() * abstractPosition, mapSize, clientID1);
            Fortress fortress2 = new Fortress(mapSize.getMapArea().getWidth() - mapSize.getBlockArea().getWidth(), mapSize.getBlockArea().getHeight() * abstractPosition, mapSize, clientID2);
            return new Fortress[]{fortress1, fortress2};
        }
    }

    private static PhysicalObject[] generateWallAroundFortress(MapSize mapSize, Fortress fortress) {
        PhysicalObject[] physicalObjects = new PhysicalObject[9];
        int x = fortress.getCoordinateX() - mapSize.getBlockArea().getWidth();
        int y = fortress.getCoordinateY() - mapSize.getBlockArea().getHeight();
        int iter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                physicalObjects[iter] = new Block(
                        x + i * mapSize.getBlockArea().getWidth(),
                        y + j * mapSize.getBlockArea().getHeight(),
                        mapSize);
                iter++;
            }
        }
        return physicalObjects;
    }

    private static void addPhysicalObjectsToGameMapIfItPossible(GameMap gameMap, PhysicalObject[] physicalObjects) {
        for (PhysicalObject physicalObject : physicalObjects) {
            gameMap.addPhysicalObjectToMap(physicalObject);
        }
    }

}
