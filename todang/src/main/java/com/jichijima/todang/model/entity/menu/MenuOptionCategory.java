package com.jichijima.todang.model.entity.menu;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "menu_option_category")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuOptionCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private Boolean isNecessary;

    @Column(nullable = false)
    private Short cntMin;

    @Column(nullable = false)
    private Short cntMax;
}
