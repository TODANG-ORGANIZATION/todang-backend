package com.jichijima.todang.model.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 필드는 JSON 응답에서 제외
public class MenuCategoryRequest {
    private Long restaurantId;
    private String name;
}
