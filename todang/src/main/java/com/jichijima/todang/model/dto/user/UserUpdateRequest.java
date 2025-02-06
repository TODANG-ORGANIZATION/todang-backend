package com.jichijima.todang.model.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {
    private String photo;
    private String name;
    private String nickname;
    private String tel;
    private String email;
}
