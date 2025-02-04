package com.jichijima.todang.model.entity.order;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long restaurantId;

    @Column (nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Short state;

    @Column(nullable = false)
    private Integer price;

    @Column
    private Integer estimatedTime;

    @Column(nullable = false)
    private LocalDateTime orderDatetime;

    @Column
    private LocalDateTime finishDatetime;

    @Column(length = 300)
    private String requirement;

    @Column(length = 20)
    private String tel;
}
