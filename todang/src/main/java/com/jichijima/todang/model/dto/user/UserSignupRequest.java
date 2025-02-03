package com.jichijima.todang.model.dto.user;
import com.jichijima.todang.model.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignupRequest {
    private String name;
    private String nickname;
    private String email;
    private String password;
    private String tel;
    private String role; // CUSTOMER 또는 OWNER (대문자로 변환 필요)

    public User.Role getRoleEnum() {
        return User.Role.valueOf(this.role.toUpperCase()); // 대문자로 변환하여 ENUM 처리
    }
}