package com.battlecity.utils;

import java.util.Random;

public class IDGeneratorUtils {

    private static final Random random = new Random(System.currentTimeMillis());

    public static long generateID() {
        return random.nextLong();
    }

}
