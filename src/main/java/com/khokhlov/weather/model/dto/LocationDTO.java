package com.khokhlov.weather.model.dto;

import lombok.Value;

@Value
public class LocationDTO {
    Integer id;
    String name;
    Double latitude;
    Double longitude;
}
