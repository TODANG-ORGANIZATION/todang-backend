package com.jichijima.todang.controller;

import com.jichijima.todang.model.dto.user.UserLoginRequest;
import com.jichijima.todang.model.dto.user.UserLoginResponse;
import com.jichijima.todang.model.dto.user.UserSignupRequest;
import com.jichijima.todang.model.entity.User;
import com.jichijima.todang.repository.UserRepository;
import com.jichijima.todang.service.UserService;
import com.jichijima.todang.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    /**
     * 회원가입 API
     */
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody UserSignupRequest request) {
        System.out.println("🚀 회원가입 요청 수신됨: " + request); // 요청 로그 추가
        User user = userService.signup(
                request.getName(),
                request.getNickname(),
                request.getEmail(),
                request.getPassword(),
                request.getTel(),
                request.getRoleEnum() // ✅ ENUM 변환된 값 전달
        );
        return ResponseEntity.ok(user);
    }

    /**
     * 닉네임 중복 확인 API
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam(value = "nickname") String nickname) {
        boolean isTaken = userService.isNicknameTaken(nickname);
        return ResponseEntity.ok(Map.of("isAvailable", !isTaken));
    }

    /**
     * 이메일 중복 확인 API
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam(value = "email") String email) {
        boolean isTaken = userService.isEmailTaken(email);
        return ResponseEntity.ok(Map.of("isAvailable", !isTaken));
    }

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new UserLoginResponse(token));
    }

    /**
     * 리프레시 토큰으로 새 액세스 토큰 발급
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다.");
        }

        String email = jwtUtil.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // ✅ 저장된 리프레시 토큰과 비교
        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 일치하지 않습니다.");
        }

        // ✅ 새 액세스 토큰 발급
        String newAccessToken = jwtUtil.generateToken(email);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}
