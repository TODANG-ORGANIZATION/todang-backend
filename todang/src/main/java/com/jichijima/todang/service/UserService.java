package com.jichijima.todang.service;

import com.jichijima.todang.model.entity.User;
import com.jichijima.todang.repository.UserRepository;
import com.jichijima.todang.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor // 🎯 의존성 자동 주입
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입 서비스 로직
     */
    public User signup(String name, String nickname, String email, String password, String tel, User.Role role) {
        // 이메일 중복 확인
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");
        }

        // 닉네임 중복 확인
        if (userRepository.existsByNickname(nickname)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다.");
        }

        // 비밀번호 해싱 후 저장
        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .name(name)
                .nickname(nickname)
                .email(email)
                .password(encodedPassword)
                .tel(tel)
                .role(role)
                .build();

        return userRepository.save(user);
    }

    /**
     * 닉네임 중복 확인
     */
    public boolean isNicknameTaken(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * 이메일 중복 확인
     */
    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * 로그인 처리 로직
     */
    public String login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일이 존재하지 않습니다.");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.generateToken(email);    // JWT 토큰 발급
    }
}
