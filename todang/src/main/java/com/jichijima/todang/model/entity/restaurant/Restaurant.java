package com.jichijima.todang.model.entity.restaurant;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private boolean isOpen;

    @Column(nullable = false)
    private boolean isLive;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lon;

    @Column(nullable = false, length = 300)
    private String description;

    @Column
    private float starAvg = 0.0f;

    @Column
    private float cleannessAvg = 0.0f;

    @Column
    private Integer reviewCnt = 0;

    @Column(nullable = false, length = 50)
    private String businessHours;

    @Column(nullable = false, length = 50)
    private String closedDays;

    @Column(nullable = false, length = 300)
    private String originCountry;

    @Column(nullable = false, length = 20)
    private String tel;

    @Column(length = 3000)
    private String photo;

    @Column(length = 3000)
    private String thumbnail;

    @Column(nullable = false, length = 10)
    private String category;
}
