package com.metazion.essence.share.kit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashKit {

    private static final Logger logger = LoggerFactory.getLogger(HashKit.class);

    public static byte[] shaBytes(byte[] bytes, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(bytes);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            logger.debug("", e);
        }
        return "".getBytes();
    }

    public static String shaHexString(byte[] bytes, String algorithm) {
        byte[] result = shaBytes(bytes, algorithm);
        return StringKit.bytesToHex(result);
    }
}
