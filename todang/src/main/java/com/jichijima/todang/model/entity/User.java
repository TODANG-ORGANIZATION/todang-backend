package com.jichijima.todang.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true) // SNS 로그인을 고려하여 nullable 허용
    private String password;

    @Column(nullable = false, unique = true)
    private String tel;

    private String userPhoto;

    @Enumerated(EnumType.ORDINAL) // 🎯 Enum 값을 숫자로 저장 (0, 1)
    @Column(nullable = false, columnDefinition = "TINYINT")
    private Role role;

    private String snsId; // SNS 로그인 시 저장되는 식별자
    private String refreshToken; // JWT 리프레시 토큰 저장

    public enum Role {
        CUSTOMER, OWNER
    }
}