package com.battlecity.models;

public interface Movable {

    long getId();

    void move(Disposition disposition);

    Area getAreaAfterMove(Disposition disposition);

}
