package com.jichijima.todang.controller;

import com.jichijima.todang.model.entity.User;
import com.jichijima.todang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 API
     */
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody Map<String, String> request) {
        User user = userService.signup(
                request.get("name"),
                request.get("nickname"),
                request.get("email"),
                request.get("password"),
                request.get("tel"),
                User.Role.valueOf(request.get("role").toUpperCase()) // CUSTOMER 또는 OWNER
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
}
