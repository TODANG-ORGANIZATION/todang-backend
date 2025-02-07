package com.jichijima.todang.model.dto.like;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 필드는 JSON 응답에서 제외
public class LikeRequest {
    private Long userId;
    private Long restaurantId;
}
