package com.jichijima.todang.model.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 가게 부분 수정 요청 DTO (PATCH)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantPatchRequest {
    private String name;
    private LocalDate startDate;
    private Boolean isOpen;
    private Boolean isLive;
    private String address;
    private Double lat;
    private Double lon;
    private String description;
    private Float starAvg;
    private Float cleannessAvg;
    private Integer reviewCnt;
    private String businessHours;
    private String closedDays;
    private String originCountry;
    private String tel;
    private String photo;
    private String thumbnail;
}
