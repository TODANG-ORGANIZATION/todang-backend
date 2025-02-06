package com.jichijima.todang.model.dto.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 필드는 JSON 응답에서 제외
public class CartItemMenuOptionRequest {
    private Long cartItemId;
    private Long menuOptionId;
}
