package com.jichijima.todang.model.entity.menu;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menus")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long restaurantId;

    @Column (nullable = false)
    private Long menuCategoryId;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 3000)
    private String menuPhoto;

    @Column(nullable = false)
    private Boolean soldout;

    @PrePersist
    public void prePersist() {
        if (soldout == null) {
            soldout = false;
        }
    }
}
