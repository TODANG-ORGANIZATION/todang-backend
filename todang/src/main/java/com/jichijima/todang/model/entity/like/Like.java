package com.jichijima.todang.model.entity.like;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurant_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column (nullable = false)
    private Long restaurantId;
}
