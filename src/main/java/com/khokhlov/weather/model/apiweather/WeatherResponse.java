package com.khokhlov.weather.model.apiweather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record WeatherResponse(
        @JsonProperty("name") String locationName,
        Coord coord,
        List<Weather> weather,
        Main main,
        Sys sys
) {
    public record Coord(
            Double lon,
            Double lat
    ) {}

    public record Weather(
            String main,
            String description,
            String icon
    ) {}

    public record Main(
            Double temp,
            @JsonProperty("feels_like") Double feelsLike,
            Integer humidity
    ) {}

    public record Sys(
            String country
    ) {}
}
