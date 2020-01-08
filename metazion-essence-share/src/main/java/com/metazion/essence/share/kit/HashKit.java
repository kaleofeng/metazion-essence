package com.metazion.essence.share.kit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashKit {

    private static final Logger logger = LoggerFactory.getLogger(HashKit.class);

    public static String shaHexString(byte[] bytes, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(bytes);
            byte[] result = digest.digest();
            return StringKit.bytesToHex(result);
        } catch (NoSuchAlgorithmException e) {
            logger.debug(e.toString());
        }
        return "";
    }
}
