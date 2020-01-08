package com.metazion.essence.share.kit;

import java.util.Random;
import java.util.UUID;

public class StringKit {

    private static final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String digits = "1234567890";

    public static String generateString(int letterLength, int digitLength) {
        int totalLength = letterLength + digitLength;
        char[] buffer = new char[totalLength];
        Random random = new Random();

        for (int i = 0; i < totalLength; ++i) {
            if (i < letterLength) {
                buffer[i] = letters.charAt(random.nextInt(letters.length()));
            } else {
                buffer[i] = digits.charAt(random.nextInt(digits.length()));
            }
        }

        return new String(buffer);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
