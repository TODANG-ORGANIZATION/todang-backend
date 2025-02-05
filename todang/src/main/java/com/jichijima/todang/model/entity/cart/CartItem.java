package com.jichijima.todang.model.entity.cart;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long restaurantId;

    @Column (nullable = false)
    private Long userId;

    @Column (nullable = false)
    private Long menuId;

    @Column(nullable = true)
    private Short quantity;
}
