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
    Integer id;
    String name;
    String country;
    String state;
    Double latitude;
    Double longitude;
}
