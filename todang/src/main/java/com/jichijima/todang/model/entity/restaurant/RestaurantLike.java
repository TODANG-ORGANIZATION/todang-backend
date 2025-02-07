package com.jichijima.todang.model.entity.restaurant;

import com.jichijima.todang.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurant_likes",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "restaurant_id"})}) // 중복 좋아요 방지
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurantId", nullable = false)
    private Restaurant restaurant;
}
