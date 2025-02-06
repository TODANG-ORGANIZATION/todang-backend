package com.jichijima.todang.model.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewSearchCriteria {
    private Long restaurantId;   // 특정 레스토랑의 리뷰 조회
    private Long userId;         // 특정 사용자의 리뷰 조회
    private Boolean photoIsNull; // 사진이 있는지 여부 필터
    private String sortBy;       // 정렬 기준 (written_datetime, star, cleanness)
    private String sortOrder;    // 정렬 순서 (asc, desc)
}


