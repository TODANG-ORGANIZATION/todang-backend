package com.jichijima.todang.model.dto.review;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 필드는 JSON 응답에서 제외
public class ReviewRequest {
    private Long orderId;
    private String photo;
    private String review;
    private String reply;
    @Min(value = 0, message = "별점은 0 이상이어야 합니다.")
    private Float star;
    @Min(value = 0, message = "위생별점은 0 이상이어야 합니다.")
    private Float cleanness;
    private LocalDateTime writtenDatetime;
}
