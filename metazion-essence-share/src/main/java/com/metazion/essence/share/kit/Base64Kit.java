package com.metazion.essence.share.kit;

import java.util.Base64;

public class Base64Kit {

    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    public static byte[] encode(byte[] bytes) {
        return encoder.encode(bytes);
    }

    public static String encodeToString(byte[] bytes) {
        return encoder.encodeToString(bytes);
    }

    public static byte[] decode(String string) {
        return decoder.decode(string);
    }

    public static String decodeToString(String string) {
        return new String(decoder.decode(string));
    }
}
