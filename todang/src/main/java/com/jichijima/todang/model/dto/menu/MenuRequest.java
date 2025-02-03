package com.jichijima.todang.model.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 필드는 JSON 응답에서 제외
public class MenuRequest {
    private Long restaurantId;
    private Long menuCategoryId;

    private String name;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;

    private String menuPhoto;
    private Boolean soldout;
}