package com.jichijima.todang.controller.user;

import com.jichijima.todang.service.user.OAuthService;
import com.jichijima.todang.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;
    private final JwtUtil jwtUtil;

    /**
     * OAuth2 로그인 (네이버 & 카카오 통합)
     */
    @PostMapping("/login/sns")
    public ResponseEntity<Map<String, String>> socialLogin(@RequestBody Map<String, String> request) {
        String provider = request.get("provider"); // "naver" 또는 "kakao"
        String code = request.get("code"); // Authorization Code

        if ("naver".equalsIgnoreCase(provider)) {
            return ResponseEntity.ok(oAuthService.loginWithNaver(code));
        } else if ("kakao".equalsIgnoreCase(provider)) {
            return ResponseEntity.ok(oAuthService.loginWithKakao(code));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "지원되지 않는 OAuth2 제공자입니다."));
        }
    }

    /**
     * OAuth2 로그인 성공 후 JWT 발급
     */
    @GetMapping("/oauth-success")
    public ResponseEntity<Map<String, String>> oauthSuccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().body(Map.of("error", "OAuth2 인증 정보가 없습니다."));
        }

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        if (email == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "OAuth2 응답에서 이메일을 찾을 수 없습니다."));
        }

        // ✅ JWT 생성
        String jwtToken = jwtUtil.generateToken(email);

        return ResponseEntity.ok(Map.of("token", jwtToken));
    }}