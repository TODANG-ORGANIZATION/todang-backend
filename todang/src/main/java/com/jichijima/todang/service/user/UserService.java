package com.jichijima.todang.service.user;

import com.jichijima.todang.model.dto.user.UserUpdateRequest;
import com.jichijima.todang.model.entity.user.User;
import com.jichijima.todang.repository.user.UserRepository;
import com.jichijima.todang.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
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
    public Map<String, String> login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일이 존재하지 않습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        // 액세스 토큰 & 리프레시 토큰 생성
        String accessToken = jwtUtil.generateToken(email);
        String refreshToken = jwtUtil.generateRefreshToken(email);

        // 리프레시 토큰을 해싱하여 저장
        String hashedRefreshToken = passwordEncoder.encode(refreshToken);
        user.setRefreshToken(hashedRefreshToken);
        userRepository.save(user);

        // 액세스 토큰 & 원본 리프레시 토큰 함께 반환
        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken // 원본 리프레시 토큰 반환 (프론트엔드에서 저장)
        );
    }

    /**
     * 로그아웃 기능 구현
     */
    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    /**
     * 사용자 정보 조회(유저 아이디)
     */
    public User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));
    }

    /**
     * 사용자 정보 조회 (이메일 기준)
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    /**
     * 회원 정보 수정 서비스 로직
     */
    public User updateUser(Long userId, String email, UserUpdateRequest request) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."));

        // 본인 확인 (토큰에서 가져온 이메일과 비교)
        if (!user.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        // 닉네임 중복 체크 (변경 시에만 실행)
        if (request.getNickname() != null && !user.getNickname().equals(request.getNickname())) {
            if (userRepository.existsByNickname(request.getNickname())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다.");
            }
        }

        // 이메일 중복 체크 (변경 시에만 실행)
        if (request.getEmail() != null && !user.getEmail().equals(request.getEmail())) {
            if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");
            }

        }

        // 값 업데이트 (null이 아닌 경우에만)
        Optional.ofNullable(request.getPhoto()).ifPresent(user::setPhoto);
        Optional.ofNullable(request.getName()).ifPresent(user::setName);
        Optional.ofNullable(request.getNickname()).ifPresent(user::setNickname);
        Optional.ofNullable(request.getTel()).ifPresent(user::setTel);
        Optional.ofNullable(request.getEmail()).ifPresent(user::setEmail);


        // 업데이트 후 저장
        return userRepository.save(user);
    }
}
