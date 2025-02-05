package com.jichijima.todang.repository.cart;

import com.jichijima.todang.model.entity.cart.CartItemMenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemMenuOptionRepository extends JpaRepository<CartItemMenuOption, Long> {
}
