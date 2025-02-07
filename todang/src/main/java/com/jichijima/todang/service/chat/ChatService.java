package com.jichijima.todang.service.chat;

import com.jichijima.todang.model.dto.chat.ChatListResponse;
import com.jichijima.todang.model.dto.chat.ChatRequest;
import com.jichijima.todang.model.dto.chat.ChatResponse;
import com.jichijima.todang.model.entity.chat.Chat;
import com.jichijima.todang.model.entity.user.User;
import com.jichijima.todang.repository.chat.ChatRepository;
import com.jichijima.todang.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatResponse createChat(ChatRequest request) {

        Chat chat = Chat.createChat(request);
        Chat savedChat = chatRepository.save(chat);

        return ChatResponse.from(savedChat);
    }

    public String getUserNickname(Long userId) {
        return userRepository.findById(userId)
                .map(User::getNickname)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public ChatListResponse getChatsByRestaurant(Long restaurantId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "sendAt"));

        Page<Chat> chatPage = chatRepository.findByRestaurantId(restaurantId, pageRequest);
        return ChatListResponse.from(chatPage.getContent());
    }

    // 새로 추가된 메소드: 특정 레스토랑의 최근 채팅 메시지 조회
    public ChatListResponse getRecentChats(Long restaurantId, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit,
                Sort.by(Sort.Direction.DESC, "sendAt"));

        Page<Chat> chatPage = chatRepository.findByRestaurantId(restaurantId, pageRequest);
        return ChatListResponse.from(chatPage.getContent());
    }
}