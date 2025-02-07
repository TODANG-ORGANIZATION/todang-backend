package com.jichijima.todang.repository.cart;

import com.jichijima.todang.model.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);
}
