package com.jichijima.todang.model.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 필드는 JSON 응답에서 제외
public class OrderMenuRequest {
    private Long orderId;
    private Long menuId;
}
