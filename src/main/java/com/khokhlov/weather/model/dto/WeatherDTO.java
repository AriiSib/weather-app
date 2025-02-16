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
    private Integer id;
    private String cityName;
    private String countryName;
    private Byte temperature;
    private Byte feelsLike;
    private String description;
    private Integer humidity;
    private String icon;
}