package com.jichijima.todang.model.entity.order;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_menus")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column (nullable = false)
    private Long menuId;
}
