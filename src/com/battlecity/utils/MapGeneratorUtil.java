package com.battlecity.utils;

import com.battlecity.models.GameMap;
import com.battlecity.models.MapSize;

public class MapGeneratorUtil {

    public static GameMap generateMap(){
        return new GameMap(MapSize.STANDART);
    }

}
