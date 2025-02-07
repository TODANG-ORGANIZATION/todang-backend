package com.jichijima.todang.model.entity.chat;

import com.jichijima.todang.model.dto.chat.ChatRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "message", nullable = false, length = 2000)
    private String message;

    @Column(name = "send_at", nullable = false)
    private LocalDateTime sendAt;

    @Column(name = "user_nickname", nullable = false, length = 20)
    private String userNickname;

    @Builder
    private Chat(Long restaurantId, Long userId, String message, String userNickname) {
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.message = message;
        this.sendAt = LocalDateTime.now();
        this.userNickname = userNickname;
    }

    // 정적 팩토리 메서드
    public static Chat createChat(ChatRequest request) {
        return Chat.builder()
                .restaurantId(request.getRestaurantId())
                .userId(request.getUserId())
                .message(request.getMessage())
                .userNickname(request.getUserNickname())
                .build();
    }
}