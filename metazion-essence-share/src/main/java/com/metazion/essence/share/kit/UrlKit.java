package com.metazion.essence.share.kit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlKit {

    private static final Logger logger = LoggerFactory.getLogger(UrlKit.class);

    public static String encode(String url, String encoding) {
        try {
            return URLEncoder.encode(url, encoding);
        } catch (UnsupportedEncodingException e) {
            logger.debug("{}", e);
        }
        return url;
    }

    public static String decode(String url, String encoding) {
        try {
            return URLDecoder.decode(url, encoding);
        } catch (UnsupportedEncodingException e) {
            logger.debug("{}", e);
        }
        return url;
    }
}
