package com.battlecity.models.properties;

import com.battlecity.models.map.Area;

public interface Iterable {

    long getId();

    void doIterate();

    Area getAreaAfterIterate();
}
