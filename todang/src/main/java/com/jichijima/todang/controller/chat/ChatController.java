package com.jichijima.todang.controller.chat;

import com.jichijima.todang.model.dto.chat.ChatListResponse;
import com.jichijima.todang.model.dto.chat.ChatRequest;
import com.jichijima.todang.model.dto.chat.ChatResponse;
import com.jichijima.todang.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    // WebSocket 채팅 메시지 처리

    /**
     * @DestinationVariable:
     * URL 경로의 변수를 추출하는 어노테이션
     * REST API의 @PathVariable과 유사한 역할
     * @Payload:
     * 클라이언트가 전송한 메시지 본문(body)을 객체로 변환
     * JSON 데이터를 자동으로 ChatRequest 객체로 변환
     * REST API의 @RequestBody와 유사한 역할
     */
    @MessageMapping("/chat.send/{restaurantId}")
    public void sendMessage(@DestinationVariable Long restaurantId,
                            @Payload ChatRequest chatRequest,
                            SimpMessageHeaderAccessor headerAccessor) {
        log.info("sendMessage: {}\n", chatRequest);
        log.info("restaurantId: {}\n", restaurantId);
        log.info("---------------------------------------------------------------------");
        ChatResponse response = chatService.createChat(chatRequest);

        // 특정 레스토랑의 채팅방으로 메시지 브로드캐스팅
        messagingTemplate.convertAndSend("/topic/restaurant." + restaurantId, response);
    }

    // REST API - 채팅 이력 조회
    @GetMapping("/api/chats/restaurants/{restaurantId}")
    public ChatListResponse getChatHistory(@PathVariable Long restaurantId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "50") int size) {
        return chatService.getChatsByRestaurant(restaurantId, page, size);
    }

    // 채팅방 입장
//    @MessageMapping("/chat.join/{restaurantId}")
//    public void joinChat(@DestinationVariable Long restaurantId,
//                         SimpMessageHeaderAccessor headerAccessor) {
//        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
//        String userNickname = chatService.getUserNickname(userId);
//
//        // 입장 메시지 생성 및 브로드캐스팅
//        ChatResponse joinMessage = ChatResponse.builder()
//                .restaurantId(restaurantId)
//                .userId(userId)
//                .message(userNickname + "님이 입장하셨습니다.")
//                .userNickname(userNickname)
//                .build();
//
//        messagingTemplate.convertAndSend("/topic/restaurant." + restaurantId, joinMessage);
//    }

    // 채팅방 퇴장
//    @MessageMapping("/chat.leave/{restaurantId}")
//    public void leaveChat(@DestinationVariable Long restaurantId,
//                          SimpMessageHeaderAccessor headerAccessor) {
//        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
//        String userNickname = chatService.getUserNickname(userId);
//
//        // 퇴장 메시지 생성 및 브로드캐스팅
//        ChatResponse leaveMessage = ChatResponse.builder()
//                .restaurantId(restaurantId)
//                .userId(userId)
//                .message(userNickname + "님이 퇴장하셨습니다.")
//                .userNickname(userNickname)
//                .build();
//
//        messagingTemplate.convertAndSend("/topic/restaurant." + restaurantId, leaveMessage);
//    }
}