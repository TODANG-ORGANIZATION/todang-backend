package com.jichijima.todang.model.entity.review;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column (length = 3000)
    private String photo;

    @Column (nullable = false, length = 300)
    private String review;

    @Column (length = 300)
    private String reply;

    @Column (nullable = false)
    private Float star;

    @Column (nullable = false)
    private Float cleanness;

    @Column (nullable = false)
    private LocalDateTime writtenDatetime;
}
