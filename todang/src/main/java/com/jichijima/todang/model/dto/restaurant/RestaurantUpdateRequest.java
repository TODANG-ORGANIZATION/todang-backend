package com.jichijima.todang.model.dto.restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 가게 전체 수정 요청 DTO (PUT)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantUpdateRequest {

    @NotBlank(message = "가게 이름은 필수 항목입니다.")
    private String name;

    @NotNull(message = "가게 오픈 날짜는 필수 항목입니다.")
    private LocalDate startDate;

    @NotBlank(message = "가게 설명은 필수 항목입니다.")
    private String description;

    @NotNull(message = "위도(lat)는 필수 항목입니다.")
    private Double lat;

    @NotNull(message = "경도(lon)는 필수 항목입니다.")
    private Double lon;

    private String businessHours;
    private String closedDays;
    private String originCountry;
    private String tel;
    private String photo;
    private String thumbnail;
}
