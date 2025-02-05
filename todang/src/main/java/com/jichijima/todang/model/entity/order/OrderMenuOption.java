package com.jichijima.todang.model.entity.order;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_menu_options")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderMenuOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderMenuId;

    @Column (nullable = false)
    private Long menuOptionId;
}
