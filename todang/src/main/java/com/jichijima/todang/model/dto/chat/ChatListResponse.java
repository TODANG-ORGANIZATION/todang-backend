package com.jichijima.todang.model.dto.chat;


import com.jichijima.todang.model.entity.chat.Chat;
import lombok.Builder;
import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ChatListResponse {
    private final List<ChatResponse> chats;

    @Builder
    private ChatListResponse(List<ChatResponse> chats) {
        this.chats = chats;
    }

    // 정적 팩토리 메서드
    public static ChatListResponse from(List<Chat> chats) {
        List<ChatResponse> chatResponses = chats.stream()
                .map(ChatResponse::from)
                .collect(Collectors.toList());
        
        return ChatListResponse.builder()
                .chats(chatResponses)
                .build();
    }
}