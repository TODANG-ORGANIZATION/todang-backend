package com.jichijima.todang.model.entity.cart;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_item_menu_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemMenuOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long cartItemId;

    @Column (nullable = false)
    private Long menuOptionId;
}
