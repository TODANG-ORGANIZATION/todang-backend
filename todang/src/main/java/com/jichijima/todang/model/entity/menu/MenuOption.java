package com.jichijima.todang.model.entity.menu;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu_option")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private Long menuOptionCategoryId;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private Integer price;
}
