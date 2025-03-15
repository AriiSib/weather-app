package com.khokhlov.weather.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    private Integer id;
    private String name;
    private String country;
    private String state;
    private Double latitude;
    private Double longitude;
}
