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
     * 네이버 로그인
     */
    @PostMapping("/login/sns")
    public ResponseEntity<Map<String, String>> naverLogin(@RequestBody Map<String, String> request) {
        String code = request.get("code"); // Authorization Code

        return ResponseEntity.ok(oAuthService.loginWithNaver(code));
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

        // ✅ JWT 생성
        String jwtToken = jwtUtil.generateToken(email);

        return ResponseEntity.ok(Map.of("token", jwtToken));
    }
}