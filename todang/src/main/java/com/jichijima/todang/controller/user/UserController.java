package com.jichijima.todang.controller.user;

import com.jichijima.todang.model.dto.user.*;
import com.jichijima.todang.model.entity.user.User;
import com.jichijima.todang.repository.user.UserRepository;
import com.jichijima.todang.service.user.UserService;
import com.jichijima.todang.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원가입 API
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserSignupRequest request) {
        System.out.println("회원가입 요청 수신됨: " + request);
        User user = userService.signup(
                request.getName(),
                request.getNickname(),
                request.getEmail(),
                request.getPassword(),
                request.getTel(),
                request.getRoleEnum()
        );
        return ResponseEntity.ok(new UserResponse(user));
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
    public ResponseEntity<CheckEmailResponse> checkEmail(@RequestParam(value = "email") String email) {
        boolean isTaken = userService.isEmailTaken(email);
        return ResponseEntity.ok(new CheckEmailResponse(!isTaken));
    }

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        Map<String, String> tokens = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new UserLoginResponse(tokens.get("accessToken"), tokens.get("refreshToken")));
    }

    /**
     * 리프레시 토큰으로 새 액세스 토큰 발급
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        // Refresh Token 검증 (유효성 + DB 비교)
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다.");
        }

        // Refresh Token에서 이메일 추출
        String email = jwtUtil.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 새 Access Token 발급
        String newAccessToken = jwtUtil.generateToken(email);

        return ResponseEntity.ok(new RefreshTokenResponse(newAccessToken));
    }


    /**
     * 로그아웃 API
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");
        }

        String email = jwtUtil.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 저장된 리프레시 토큰(해싱)과 비교
        if (!passwordEncoder.matches(refreshToken, user.getRefreshToken())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 일치하지 않습니다.");
        }

        userService.logout(email);
        return ResponseEntity.ok(Map.of("message", "로그아웃 되었습니다."));
    }

    /**
     * 사용자 정보 조회 API(본인만 가능)
     */
    @GetMapping("/{user_id}")
    public ResponseEntity<UserResponse> getUserInfo(
            @PathVariable("user_id") long userId,
            @AuthenticationPrincipal UserDetails userDetails) {

        // SecurityContext에서 현재 로그인한 사용자의 이메일 가져오기
        String authenticatedEmail = userDetails.getUsername();

        // 이메일로 사용자 ID 조회
        User authenticatedUser = userService.getUserByEmail(authenticatedEmail);
        long authenticatedUserId = authenticatedUser.getId();

        //본인만 조회 가능하도록 체크
        if (userId != authenticatedUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        User user = userService.getUserById(userId);
        return ResponseEntity.ok(new UserResponse(user));
    }

    /**
     * 회원 정보 수정 API
     */
    @PutMapping("/{user_id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable("user_id") Long userId,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserUpdateRequest request) {

        // 토큰에서 이메일 추출
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);

        // 본인 확인 후 업데이트 수행
        User updatedUser = userService.updateUser(userId, email, request);

        return ResponseEntity.ok(new UserResponse(updatedUser));
    }
}
