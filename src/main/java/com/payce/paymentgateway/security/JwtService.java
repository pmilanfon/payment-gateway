package com.payce.paymentgateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Password;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String KEY = "bddfdfd80e7ae33f1a89fc9aa761c87153fca26420b2a6fdabb1eb0a59731418";
    public static final int EXPIRE_TIME = 1000 * 60 * 10;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        Claims claims = extractClaims(token);
        return claimsTFunction.apply(claims);
    }

    public Claims extractClaims(String token) {
        Password password = Keys.password(KEY.toCharArray());
        return Jwts.parser()
                .decryptWith(password)
                .build()
                .parseEncryptedClaims(token)
                .getPayload();
    }

    public String createToken(UserDetails userDetails) {
        return createToken(Map.of(), userDetails);
    }

    public String createToken(Map<String, Object> additionalClaims, UserDetails userDetails) {
        Password password = Keys.password(KEY.toCharArray());
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(additionalClaims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .encryptWith(password, Jwts.KEY.PBES2_HS256_A128KW, Jwts.ENC.A128CBC_HS256)
                .compact();
    }

    public boolean isValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && isExpired(token);
    }

    public boolean isExpired(String token) {
        return new Date().before(extractClaim(token, Claims::getExpiration));
    }
}
