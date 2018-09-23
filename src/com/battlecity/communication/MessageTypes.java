package com.battlecity.communication;


public class MessageTypes {

    public static final byte[] TYPE_MOVE = "move".getBytes();
    public static final byte[] TYPE_SHOOT = "shoot".getBytes();

    public static final byte[] TYPE_AVAILABILITY = "availability".getBytes();
    public static final byte[] TYPE_MAP = "map".getBytes();

    public static final byte[] TYPE_FINISH = "finishGame".getBytes();

    public static final byte[] KEY_CONNECTION_ID = "connectionID".getBytes();
    public static final byte[] KEY_DISPOSITION = "disposition".getBytes();
    public static final byte[] KEY_GAME_MAP = "gameMap".getBytes();

//    public static final byte[] TEST = "EST".getBytes();

}
