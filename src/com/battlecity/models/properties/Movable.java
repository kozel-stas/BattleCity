package com.battlecity.models.properties;

import com.battlecity.models.map.Area;
import com.battlecity.models.map.Disposition;

public interface Movable {

    long getId();

    void move(Disposition disposition);

    Area getAreaAfterMove(Disposition disposition);

}
