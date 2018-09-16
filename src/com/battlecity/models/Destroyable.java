package com.battlecity.models;

/**
 * For objects that can be destroyed.
 */
public interface Destroyable {

    long getId();

    boolean destroyObject();

}
