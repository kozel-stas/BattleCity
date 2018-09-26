package com.battlecity.utils;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Comparator;

public class BytesUtils {

    public static final BytesComparator BYTES_COMPARATOR = new BytesComparator();

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
                (byte) ((data >> 0) & 0xff),
                (byte) ((data >> 8) & 0xff),
                (byte) ((data >> 16) & 0xff),
                (byte) ((data >> 24) & 0xff),
                (byte) ((data >> 32) & 0xff),
                (byte) ((data >> 40) & 0xff),
                (byte) ((data >> 48) & 0xff),
                (byte) ((data >> 56) & 0xff),
        };
    }

    public static byte[] toByteArray(int data) {
        return new byte[]{
                (byte) (data >>> 0),
                (byte) (data >>> 8),
                (byte) (data >>> 16),
                (byte) (data >>> 24),
        };
    }

    public static Long byteArrayToLong(byte[] data) {
        if (data != null) {
            long value = 0;
            value += (long) (data[7] & 0x000000FF) << 56;
            value += (long) (data[6] & 0x000000FF) << 48;
            value += (long) (data[5] & 0x000000FF) << 40;
            value += (long) (data[4] & 0x000000FF) << 32;
            value += (long) (data[3] & 0x000000FF) << 24;
            value += (long) (data[2] & 0x000000FF) << 16;
            value += (long) (data[1] & 0x000000FF) << 8;
            value += (long) (data[0] & 0x000000FF);
            return value;
        }
        return null;
    }

    public static Integer byteArrayToInteger(byte[] data) {
        if (data != null) {
            int value = 0;
            value += (data[3] & 0xFF) << 24;
            value += (data[2] & 0xFF) << 16;
            value += (data[1] & 0xFF) << 8;
            value += (data[0] & 0xFF);
            return value;
        }
        return null;
    }

    private static class BytesComparator implements Comparator<byte[]>, Serializable {

        @Override
        public int compare(byte[] left, byte[] right) {
            for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
                int a = (left[i] & 0xff);
                int b = (right[j] & 0xff);
                if (a != b) {
                    return a - b;
                }
            }
            return left.length - right.length;
        }

    }

}
