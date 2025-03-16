package com.infinite.busTicket.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private String SECRET_KEY = "45PfHxuCYQcfCR3zkRPZjizLjaHWMD9R5vw+AoYjFHAWkcKDxo/SlYIi5tvB2RV4";

    public String generateToken(String username)
    {
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject)
    {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("type","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*30))  //30 min
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey()
    {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String token)
    {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token)
    {
        return Jwts.parser().verifyWith(getSigningKey())
                .build().parseSignedClaims(token)
                .getPayload();
    }

    private Date expirationDate(String token)
    {
        return extractClaims(token).getExpiration();
    }

    public boolean isExpired(String token)
    {
        return expirationDate(token).before(new Date());
    }

    public boolean validToken(String token)
    {
        return !(isExpired(token));
    }
}
