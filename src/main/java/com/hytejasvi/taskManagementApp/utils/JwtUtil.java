package com.hytejasvi.taskManagementApp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {

    private static String SECRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";

    private static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    
    public String generateTokens(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createTokens(claims, userName);
    }

    private String createTokens(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 50)) // 50 minutes expiration time
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUserName(String token) {
        Claims claims = extractAllClaims(token);
        log.info("Claims: {}",claims);
        return claims.getSubject();
    }

    public static Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public static boolean validToken(String jwt) {
        return !isTokenExpired(jwt);
    }

    private static Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
