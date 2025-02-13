package com.khokhlov.weather.model.apiweather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenWeatherResponse {
    private String name;
    private Coord coord;
    private List<Weather> weather;
    private Main main;
    private Sys sys;

    @Data
    public static class Coord {
        private Double lon;
        private Double lat;
    }

    @Data
    public static class Weather {
        private String main;
        private String description;
        private String icon;
    }

    @Data
    public static class Main {
        private Double temp;

        @JsonProperty("feels_like")
        private Double feelsLike;
        private Integer humidity;
    }

    @Data
    public static class Sys {
        private String country;
    }
}
