package com.jichijima.todang.model.dto.user;

import com.jichijima.todang.model.entity.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String tel;
    private String photo;
    private String role;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.tel = user.getTel();
        this.photo = user.getPhoto();
        this.role = convertRoleToString(user.getRole());
    }

    private String convertRoleToString(User.Role role) {
        if (role == null) return "UNKNOWN";
        return role.name(); // CUSTOMER or OWNER 반환
    }
}
