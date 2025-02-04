package com.jichijima.todang.model.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {
    private String profilePhoto;
    private String name;
    private String nickName;
    private String tel;
    private String email;
}
