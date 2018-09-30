package com.battlecity.models.properties;

import com.battlecity.models.map.PhysicalObject;

public interface Physical {

    PhysicalObject getPhysicalObject();

    default long getId() {
        return getPhysicalObject().getId();
    }

}
