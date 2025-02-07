package com.jichijima.todang.model.dto.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 필드는 JSON 응답에서 제외
public class CartItemRequest {
    private Long restaurantId;
    private Long userId;
    private Long menuId;
    @Min(value = 0, message = "수량은 0이상이어야 합니다.")
    private Short quantity;
}
