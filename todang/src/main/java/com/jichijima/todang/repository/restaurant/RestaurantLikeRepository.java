package com.jichijima.todang.repository.restaurant;

import com.jichijima.todang.model.entity.restaurant.RestaurantLike;
import com.jichijima.todang.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantLikeRepository extends JpaRepository<RestaurantLike, Long> {
    List<RestaurantLike> findByUser(User user);
}
