package com.jichijima.todang.repository.chat;


import com.jichijima.todang.model.entity.chat.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Page<Chat> findByRestaurantId(Long restaurantId, Pageable pageable);

    @Query("SELECT c FROM Chat c WHERE c.restaurantId = :restaurantId AND c.sendAt > :time")
    List<Chat> findRecentChatsByRestaurantId(Long restaurantId, LocalDateTime time);
}