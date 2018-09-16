package com.battlecity.utils;

import java.util.Random;

public class IDGeneratorUtil {

    private static final Random random = new Random(System.currentTimeMillis());

    public static long generate() {
        return random.nextLong();
    }

}
