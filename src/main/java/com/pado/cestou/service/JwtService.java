package com.pado.cestou.service;

import com.pado.cestou.model.Employee;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretkey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Employee employee) {
        return Jwts
                .builder()
                .subject(employee.getId().toString())
                .claim("email", employee.getEmail())
                .claim("name", employee.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60*24))
                .signWith(getSecretkey())
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretkey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());
    }
}
