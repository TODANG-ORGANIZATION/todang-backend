package com.jichijima.todang.repository.cart;

import com.jichijima.todang.model.entity.cart.CartItemMenuOption;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CartItemMenuOptionRepository extends JpaRepository<CartItemMenuOption, Long> {
    List<CartItemMenuOption> findByCartItemId(Long cartItemId);
    // cartItemId 삭제시 일괄 삭제용.
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItemMenuOption c WHERE c.cartItemId = :cartItemId")
    int deleteByCartItemId(@Param("cartItemId") Long cartItemId);
}
