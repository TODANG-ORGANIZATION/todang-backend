package com.jichijima.todang.model.dto.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChatRequest {
    private Long restaurantId;
    private Long userId;
    private String message;
    private String userNickname;
}