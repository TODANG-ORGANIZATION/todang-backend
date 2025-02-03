package com.jichijima.todang.model.entity.menu;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "menu_category")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Column(nullable = false)
    private String name;
}
