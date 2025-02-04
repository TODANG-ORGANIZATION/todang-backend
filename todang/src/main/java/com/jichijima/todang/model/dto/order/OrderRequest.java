package com.jichijima.todang.model.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 필드는 JSON 응답에서 제외
public class OrderRequest {
    private Long restaurantId;
    private Long userId;
    @Min(value = 0, message = "상태는 0 이상이어야 합니다.")
    private Short state;
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;
    @Min(value = 0, message = "예상소요시간은 0 이상이어야 합니다.")
    private Integer estimatedTime;
    private LocalDateTime orderDatetime;
    private LocalDateTime finishDatetime;
    private String requirement;
    private String tel;
}
