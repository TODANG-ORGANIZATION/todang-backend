package com.jichijima.todang.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckEmailResponse {
    private boolean isAvailable;
}
