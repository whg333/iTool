/**
 * 
 */
package com.why.tool.codec;

public class BitConverter {

    public static byte[] int2bytes(int intValue) {
        byte[] result = new byte[4];
        for (int i = 3; i >= 0; i--) {
            result[i] = (byte) (intValue & 0xff);
            intValue >>= 8;
        }
        return result;
    }

    public static int bytes2int(byte[] bytes, int pos) {
        int result = 0;
        result |= (bytes[pos] & 0xff);
        for (int i = pos; i < pos + 4; i++) {
            result <<= 8;
            result |= (bytes[i] & 0xff);
        }
        return result;
    }

    public static byte[] long2bytes(long input) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (input & 0xff);
            input >>= 8;
        }
        return result;
    }

    public static long bytes2long(byte[] bytes, int pos) {
        long result = 0;
        result |= (bytes[pos] & 0xff);
        for (int i = pos; i < pos + 8; i++) {
            result <<= 8;
            result |= (bytes[i] & 0xff);
        }
        return result;
    }

    public static int[] bytes2int(byte[] bytes) {
        int[] results = new int[bytes.length / 4];
        int pos = 0;
        for (int i = 0; i < results.length; i++) {
            results[i] = bytes2int(bytes, pos);
            pos += 4;

        }
        return results;
    }
}
