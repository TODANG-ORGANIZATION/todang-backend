package com.jichijima.todang.util;

import com.jichijima.todang.model.entity.user.User;
import com.jichijima.todang.repository.user.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;
    private final UserRepository userRepository;

    public JwtUtil(
            @Value("${jwt.secretKey}") String secretKey,
            @Value("${jwt.access-token-expiry}") long accessTokenValidity,
            @Value("${jwt.refresh-token-expiry}") long refreshTokenValidity,
            UserRepository userRepository
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.userRepository = userRepository;
    }

    // 새 Access Token 생성
    public String generateToken(String email) {
        return generateToken(email, accessTokenValidity);
    }

    // 새 RefreshToken 생성
    public String generateRefreshToken(String email) {
        String refreshToken = generateToken(email, refreshTokenValidity);

        // Refresh Token을 DB에 저장
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
        });

        return refreshToken;
    }

    //공통 토큰 생성 메서드
    private String generateToken(String email, long validity) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //Access Token 검증 (유효성 검사만 수행)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("토큰이 만료되었습니다.");
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("유효하지 않은 토큰입니다.");
            return false;
        }
    }

    //RefreshToken 검증(JWT 유호성 + DB  저장값 비교)
    public boolean validateRefreshToken(String refreshToken) {
        try {
            // JWT 유효성 검사
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);

            // Refresh Token이 DB에 존재하는지 확인
            String email = extractEmail(refreshToken);
            Optional<User> user = userRepository.findByEmail(email);


            //DB에 저장된 Refresh Token과 비교
            return user.map(u -> refreshToken.equals(u.getRefreshToken())).orElse(false);

        } catch (ExpiredJwtException e) {
            System.err.println("Refresh Token이 만료되었습니다.");
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("유효하지 않은 Refresh Token입니다.");
            return false;
        }
    }


    // JWT에서 이메일 추출 (예외 발생 시 null 반환)
    public String extractEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            System.err.println("토큰이 만료되어 이메일을 추출할 수 없습니다.");
            throw new RuntimeException("토큰이 만료되었습니다.");
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("유효하지 않은 토큰으로 이메일을 추출할 수 없습니다.");
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
    }
}
