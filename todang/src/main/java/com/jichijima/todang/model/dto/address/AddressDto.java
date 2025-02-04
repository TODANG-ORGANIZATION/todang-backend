package com.jichijima.todang.model.dto.address;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    private Long id;
    private Long userId;
    private String name;
    private Boolean isPrimary;
    private String address;
    private String detail;
}
