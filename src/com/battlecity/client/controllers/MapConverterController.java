package com.battlecity.client.controllers;

import com.battlecity.models.GameMap;

public class MapConverterController {

    private static MapConverterController mapConverterController = null;

    public static MapConverterController getInstance() {
        synchronized (mapConverterController) {
            if (mapConverterController == null) {
                return new MapConverterController();
            }
            return mapConverterController;
        }
    }

    public void processInputMap(GameMap gameMap){
        System.out.println(gameMap);
    }

}
