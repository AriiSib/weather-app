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

    private static class Coord {
        private double lon;
        private double lat;
    }

    private static class Weather {
        private String main;
        private String description;
        private String icon;
    }

    private static class Main {
        private double temp;

        @JsonProperty("feels_like")
        private double feelsLike;
        private int humidity;
    }

    private static class Sys {
        private String country;
    }
}
