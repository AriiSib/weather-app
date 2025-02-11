package com.khokhlov.weather.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDTO {
    private String cityName;
    private String countryName;
    private Double temperature;
    private Double feelsLike;
    private String description;
    private Integer humidity;
    private String icon;
}