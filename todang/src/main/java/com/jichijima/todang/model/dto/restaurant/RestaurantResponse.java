package com.jichijima.todang.model.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 가게 목록 조회 응답
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {
    private Long id;
    private String name;
    private boolean isOpen;
    private boolean isLive;
    private String address;
    private String category;
    private float starAvg;
    private int reviewCnt;
    private String photo;
}
