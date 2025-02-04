package com.jichijima.todang.model.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuOptionCategoryRequest {
    private Long menuId;
    private String name;
    private Boolean isNecessary;
    @Min(value = 0, message = "최소 갯수는 0 이상이어야 합니다.")
    private Short cntMin;
    @Min(value = 1, message = "최대 갯수는 1 이상이어야 합니다.")
    private Short cntMax;
}
