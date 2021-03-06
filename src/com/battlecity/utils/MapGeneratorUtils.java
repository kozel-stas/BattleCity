package com.battlecity.utils;

import com.battlecity.models.blocks.Block;
import com.battlecity.models.blocks.Fortress;
import com.battlecity.models.blocks.NonDestroyableBlock;
import com.battlecity.models.map.GameMap;
import com.battlecity.models.properties.GameProperties;
import com.battlecity.models.properties.Physical;

import java.util.Random;

public class MapGeneratorUtils {

    private static final Random random = new Random(System.currentTimeMillis());

    public static GameMap generateMap(long clientID1, long clientID2) {
        Fortress[] fortresses = getAreaForFortresses(GameProperties.STANDART, clientID1, clientID2);
        GameMap gameMap = new GameMap(GameProperties.STANDART, fortresses[0], fortresses[1]);
        Physical[] physicalObjects1 = generateWallAroundFortress(GameProperties.STANDART, fortresses[0]);
        Physical[] physicalObjects2 = generateWallAroundFortress(GameProperties.STANDART, fortresses[1]);
        addPhysicalObjectsToGameMapIfItPossible(gameMap, physicalObjects1);
        addPhysicalObjectsToGameMapIfItPossible(gameMap, physicalObjects2);
        generateRandomDestroyableObjectsAndAddIfItPossible(GameProperties.STANDART, gameMap);
        generateRandomNonDestroyableObjectsAndAddIfItPossible(GameProperties.STANDART, gameMap);
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

    private static Physical[] generateWallAroundFortress(GameProperties gameProperties, Fortress fortress) {
        Physical[] physicalObjects = new Physical[9];
        int x = fortress.getPhysicalObject().getCoordinateX() - gameProperties.getBlockArea().getWidth();
        int y = fortress.getPhysicalObject().getCoordinateY() - gameProperties.getBlockArea().getHeight();
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

    private static void addPhysicalObjectsToGameMapIfItPossible(GameMap gameMap, Physical[] physicalObjects) {
        for (Physical physicalObject : physicalObjects) {
            gameMap.addPhysicalObjectToMap(physicalObject);
        }
    }

    private static void generateRandomDestroyableObjectsAndAddIfItPossible(GameProperties gameProperties, GameMap gameMap) {
        int max = random.nextInt(gameProperties.getMaxDestroyableBlock() - 5); // 5 - a wall size
        int maxRandX = (int) gameProperties.getMapArea().getWidth() / gameProperties.getBlockArea().getWidth();
        int maxRandY = (int) gameProperties.getMapArea().getHeight() / gameProperties.getBlockArea().getHeight();
        for (int i = 0; i < max; i++) {
            int x = random.nextInt(maxRandX);
            int y = random.nextInt(maxRandY);
            Physical physicalObject = new Block(
                    x * gameProperties.getBlockArea().getWidth(),
                    y * gameProperties.getBlockArea().getHeight(),
                    gameProperties);
            if (!gameMap.addPhysicalObjectToMap(physicalObject)) {
                i--;
            }
        }
    }

    private static void generateRandomNonDestroyableObjectsAndAddIfItPossible(GameProperties gameProperties, GameMap gameMap) {
        int max = random.nextInt(gameProperties.getMaxNonDestroyableBlock());
        int maxRandX = (int) gameProperties.getMapArea().getWidth() / gameProperties.getBlockArea().getWidth();
        int maxRandY = (int) gameProperties.getMapArea().getHeight() / gameProperties.getBlockArea().getHeight();
        for (int i = 0; i < max; i++) {
            int x = random.nextInt(maxRandX);
            int y = random.nextInt(maxRandY);
            Physical physicalObject = new NonDestroyableBlock(
                    x * gameProperties.getBlockArea().getWidth(),
                    y * gameProperties.getBlockArea().getHeight(),
                    gameProperties);
            if (!gameMap.addPhysicalObjectToMap(physicalObject)) {
                i--;
            }
        }
    }

}
