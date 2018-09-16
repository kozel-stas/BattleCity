package com.battlecity.utils;

import java.nio.charset.Charset;

public class BytesUtils {

    public static String byteArrayToString(byte[] data) {
        if (data != null) {
            return new String(data, Charset.defaultCharset());
        }
        return null;
    }

    public static byte[] toByteArray(String data) {
        if (data == null) {
            return null;
        }
        return data.getBytes(Charset.defaultCharset());
    }

    public static byte[] toByteArray(long data) {
        return new byte[]{
                (byte) ((data >> 56) & 0xff),
                (byte) ((data >> 48) & 0xff),
                (byte) ((data >> 40) & 0xff),
                (byte) ((data >> 32) & 0xff),
                (byte) ((data >> 24) & 0xff),
                (byte) ((data >> 16) & 0xff),
                (byte) ((data >> 8) & 0xff),
                (byte) ((data >> 0) & 0xff),
        };
    }

    public static byte[] toByteArray(int data) {
        return new byte[]{
                (byte) (data >> 24),
                (byte) (data >> 16),
                (byte) (data >> 8),
                (byte) (data >> 0),
        };
    }

    public static Long byteArrayToLong(byte[] data) {
        if (data != null) {
            long value = 0;
            value += (long) (data[7] & 0x000000FF) << 56;
            value += (long) (data[6] & 0x000000FF) << 48;
            value += (long) (data[5] & 0x000000FF) << 40;
            value += (long) (data[4] & 0x000000FF) << 32;
            value += (data[3] & 0x000000FF) << 24;
            value += (data[2] & 0x000000FF) << 16;
            value += (data[1] & 0x000000FF) << 8;
            value += (data[0] & 0x000000FF);
            return value;
        }
        return null;
    }

    public static Integer byteArrayToInteger(byte[] data) {
        if (data != null) {
            int value = 0;
            value += (data[3] & 0x000000FF) << 24;
            value += (data[2] & 0x000000FF) << 16;
            value += (data[1] & 0x000000FF) << 8;
            value += (data[0] & 0x000000FF);
            return value;
        }
        return null;
    }

}
