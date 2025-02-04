package com.jichijima.todang.controller.user;

import com.jichijima.todang.service.user.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    /**
     * 네이버 로그인
     */
    @PostMapping("/login/sns")
    public ResponseEntity<Map<String, String>> naverLogin(@RequestBody Map<String, String> request) {
        String code = request.get("code"); // Authorization Code

        return ResponseEntity.ok(oAuthService.loginWithNaver(code));
    }
}