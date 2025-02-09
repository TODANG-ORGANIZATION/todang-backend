package com.jichijima.todang.model.dto.chat;


import com.jichijima.todang.model.entity.chat.Chat;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ChatResponse {
    private final Long id;
    private final Long restaurantId;
    private final Long userId;
    private final String message;
    private final LocalDateTime sendAt;
    private final String userNickname;

    @Builder
    private ChatResponse(Long id, Long restaurantId, Long userId, String message, 
                        LocalDateTime sendAt, String userNickname) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.message = message;
        this.sendAt = sendAt;
        this.userNickname = userNickname;
    }

    // 정적 팩토리 메서드
    public static ChatResponse from(Chat chat) {
        return ChatResponse.builder()
                .id(chat.getId())
                .restaurantId(chat.getRestaurantId())
                .userId(chat.getUserId())
                .message(chat.getMessage())
                .sendAt(chat.getSendAt())
                .userNickname(chat.getUserNickname())
                .build();
    }
}