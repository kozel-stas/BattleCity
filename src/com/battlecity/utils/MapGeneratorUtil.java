package com.battlecity.utils;

import com.battlecity.models.GameMap;
import com.battlecity.models.GameProperties;
import com.battlecity.models.PhysicalObject;
import com.battlecity.models.blocks.Block;
import com.battlecity.models.blocks.Fortress;

import java.util.Random;

public class MapGeneratorUtil {

    public static GameMap generateMap(long clientID1, long clientID2) {
        Fortress[] fortresses = getAreaForFortresses(GameProperties.STANDART, clientID1, clientID2);
        GameMap gameMap = new GameMap(GameProperties.STANDART, fortresses[0], fortresses[1]);
        PhysicalObject[] physicalObjects1 = generateWallAroundFortress(GameProperties.STANDART, fortresses[0]);
        PhysicalObject[] physicalObjects2 = generateWallAroundFortress(GameProperties.STANDART, fortresses[1]);
        addPhysicalObjectsToGameMapIfItPossible(gameMap, physicalObjects1);
        addPhysicalObjectsToGameMapIfItPossible(gameMap, physicalObjects2);
        return gameMap;
    }

    private static Fortress[] getAreaForFortresses(GameProperties gameProperties, long clientID1, long clientID2) {
        Random random = new Random();
        if (random.nextBoolean()) {
            int end = (int) gameProperties.getMapArea().getWidth() / gameProperties.getBlockArea().getWidth();
            int abstractPosition = random.nextInt(end);
            Fortress fortress1 = new Fortress(gameProperties.getBlockArea().getWidth() * abstractPosition, 0, gameProperties, clientID1);
            Fortress fortress2 = new Fortress(gameProperties.getBlockArea().getWidth() * abstractPosition, gameProperties.getMapArea().getHeight() - gameProperties.getBlockArea().getHeight(), gameProperties, clientID2);
            return new Fortress[]{fortress1, fortress2};
        } else {
            int end = (int) gameProperties.getMapArea().getHeight() / gameProperties.getBlockArea().getHeight();
            int abstractPosition = random.nextInt(end);
            Fortress fortress1 = new Fortress(0, gameProperties.getBlockArea().getHeight() * abstractPosition, gameProperties, clientID1);
            Fortress fortress2 = new Fortress(gameProperties.getMapArea().getWidth() - gameProperties.getBlockArea().getWidth(), gameProperties.getBlockArea().getHeight() * abstractPosition, gameProperties, clientID2);
            return new Fortress[]{fortress1, fortress2};
        }
    }

    private static PhysicalObject[] generateWallAroundFortress(GameProperties gameProperties, Fortress fortress) {
        PhysicalObject[] physicalObjects = new PhysicalObject[9];
        int x = fortress.getCoordinateX() - gameProperties.getBlockArea().getWidth();
        int y = fortress.getCoordinateY() - gameProperties.getBlockArea().getHeight();
        int iter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                physicalObjects[iter] = new Block(
                        x + i * gameProperties.getBlockArea().getWidth(),
                        y + j * gameProperties.getBlockArea().getHeight(),
                        gameProperties);
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
