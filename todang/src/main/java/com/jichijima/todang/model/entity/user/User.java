package com.jichijima.todang.model.entity.user;

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

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String tel;

    private String photo;

    @Convert(converter = RoleConverter.class) // 명시적 변환기 적용
    @Column(nullable = false)
    private Role role;

    @Column(length = 500)
    private String refreshToken;

    public enum Role {
        CUSTOMER, OWNER
    }

    // OAuth2 사용자 업데이트
    public User update(String nickname, String profileImage, String mobile) {
        this.nickname = nickname;
        this.photo = profileImage;
        this.tel = mobile;
        return this;
    }

}
