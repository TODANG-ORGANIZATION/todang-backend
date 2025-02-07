package com.jichijima.todang.model.dto.restaurant;

import com.jichijima.todang.model.entity.restaurant.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
/**
 * 가게 상세 조회 응답
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDetailResponse {
    private Long id;
    private String name;
    private LocalDate startDate;
    private boolean isOpen;
    private boolean isLive;
    private String address;
    private double lat;
    private double lon;
    private String description;
    private float starAvg;
    private float cleannessAvg;
    private int reviewCnt;
    private String businessHours;
    private String closedDays;
    private String originCountry;
    private String tel;
    private String photo;
    private String thumbnail;
    private String category;

    public static RestaurantDetailResponse fromEntity(Restaurant restaurant) {
        return RestaurantDetailResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .startDate(restaurant.getStartDate())
                .isOpen(restaurant.isOpen())
                .isLive(restaurant.isLive())
                .address(restaurant.getAddress())
                .lat(restaurant.getLat())
                .lon(restaurant.getLon())
                .description(restaurant.getDescription())
                .starAvg(restaurant.getStarAvg())
                .cleannessAvg(restaurant.getCleannessAvg())
                .reviewCnt(restaurant.getReviewCnt())
                .businessHours(restaurant.getBusinessHours())
                .closedDays(restaurant.getClosedDays())
                .originCountry(restaurant.getOriginCountry())
                .tel(restaurant.getTel())
                .photo(restaurant.getPhoto())
                .thumbnail(restaurant.getThumbnail())
                .category(restaurant.getCategory())
                .build();
    }
}
