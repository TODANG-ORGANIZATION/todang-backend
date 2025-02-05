package com.jichijima.todang.repository.like;

import com.jichijima.todang.model.entity.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndRestaurantId(Long userId, Long restaurantId);
}
