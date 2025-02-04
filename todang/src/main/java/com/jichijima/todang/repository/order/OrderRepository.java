package com.jichijima.todang.repository.order;

import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.model.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByRestaurantId(Long restaurantId);
    List<Order> findByUserIdAndRestaurantId(Long userId, Long restaurantId);
}
