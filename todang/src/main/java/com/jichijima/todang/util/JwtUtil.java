package com.jichijima.todang.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long validityInMilliseconds;

    public JwtUtil(
            @Value("${jwt.secretKey}") String secretKey,
            @Value("${jwt.validate-time}") long validityInMilliseconds
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    // ✅ JWT 토큰 생성
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ JWT 토큰에서 이메일 추출
    public String extractEmail(String token) {
        try {
            System.out.println("Received Token: " + token);
            String email = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            System.out.println("Extracted Email: " + email);
            return email;
        } catch (Exception e) {
            System.err.println("JWT Parsing Error: " + e.getMessage());
            return null;
        }
    }


    // ✅ JWT 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
