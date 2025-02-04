package com.jichijima.todang.model.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuOptionRequest {
    private Long menuId;
    private Long menuOptionCategoryId;
    private String name;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;
}
