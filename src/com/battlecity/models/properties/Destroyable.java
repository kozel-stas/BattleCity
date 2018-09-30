package com.battlecity.models.properties;

/**
 * For objects that can be destroyed.
 */
public interface Destroyable {

    long getId();

    boolean destroyObject();

}
