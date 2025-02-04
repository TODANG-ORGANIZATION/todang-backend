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

    @Column(nullable = false, unique = true)
    private String tel;

    private String userPhoto;

    @Enumerated(EnumType.STRING) // ENUM을 문자열로 저장
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
        this.userPhoto = profileImage;
        this.tel = mobile;
        return this;
    }

}
