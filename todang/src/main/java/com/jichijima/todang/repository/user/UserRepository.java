package com.jichijima.todang.repository.user;

import com.jichijima.todang.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);   // 이메일로 회원조회
    boolean existsByNickname(String nickname); // 닉네임 중복 여부 확인
}
