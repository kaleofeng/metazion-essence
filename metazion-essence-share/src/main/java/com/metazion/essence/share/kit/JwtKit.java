package com.metazion.essence.share.kit;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class JwtKit {

    private static final Logger logger = LoggerFactory.getLogger(JwtKit.class);

    private static final String BEARER = "Bearer ";

    public static String generateToken(Map<String, Object> claims, String secret, Duration duration) {
        long expireMS = Instant.now().plusNanos(duration.toNanos()).toEpochMilli();
        Date expireDate = new Date(expireMS);
        return Jwts.builder()
                   .setClaims(claims)
                   .setExpiration(expireDate)
                   .signWith(SignatureAlgorithm.HS256, secret)
                   .compact();
    }

    public static Optional<Map<String, Object>> validateToken(String token, String secret) {
        String jwt = token;
        if (token.startsWith(BEARER)) {
            jwt = token.replace(BEARER, "");
        }

        try {
            Claims claims = Jwts.parser()
                                .setSigningKey(secret)
                                .parseClaimsJws(jwt)
                                .getBody();
            return Optional.ofNullable(claims);
        } catch (Exception e) {
            logger.debug(e.toString());
        }

        return Optional.empty();
    }
}
