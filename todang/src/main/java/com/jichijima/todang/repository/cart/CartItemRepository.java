package com.jichijima.todang.repository.cart;

import com.jichijima.todang.model.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
