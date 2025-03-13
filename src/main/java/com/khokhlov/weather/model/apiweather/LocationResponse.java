package com.khokhlov.weather.model.apiweather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LocationResponse(
        @JsonProperty("name") String locationName,
        Double lat,
        Double lon,
        String country,
        String state
) {}
