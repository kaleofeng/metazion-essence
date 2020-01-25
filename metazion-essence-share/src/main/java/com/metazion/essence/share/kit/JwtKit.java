package com.metazion.essence.share.kit;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class JwtKit {

    private static final Logger logger = LoggerFactory.getLogger(JwtKit.class);

    public static String generateHashToken(Map<String, Object> claims, String key, Duration duration) {
        long expireMS = Instant.now().plusNanos(duration.toNanos()).toEpochMilli();
        Date expireDate = new Date(expireMS);
        return Jwts.builder()
                   .setClaims(claims)
                   .setExpiration(expireDate)
                   .signWith(SignatureAlgorithm.HS256, key)
                   .compact();
    }

    public static Optional<Map<String, Object>> validateHashToken(String token, String key) {
        try {
            Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token)
                                .getBody();
            return Optional.ofNullable(claims);
        } catch (Exception e) {
            logger.debug(e.toString());
        }

        return Optional.empty();
    }

    public static String generateRsaToken(Map<String, Object> claims, Key key, Duration duration) {
        long expireMS = Instant.now().plusNanos(duration.toNanos()).toEpochMilli();
        Date expireDate = new Date(expireMS);
        return Jwts.builder()
                   .setClaims(claims)
                   .setExpiration(expireDate)
                   .signWith(SignatureAlgorithm.RS256, key)
                   .compact();
    }

    public static Optional<Map<String, Object>> validateRsaToken(String token, Key key) {
        try {
            Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token)
                                .getBody();
            return Optional.ofNullable(claims);
        } catch (Exception e) {
            logger.debug(e.toString());
        }

        return Optional.empty();
    }
}
