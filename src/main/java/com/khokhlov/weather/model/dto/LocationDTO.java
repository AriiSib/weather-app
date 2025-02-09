package com.khokhlov.weather.model.dto;

import com.khokhlov.weather.model.entity.Location;
import lombok.Value;

@Value
public class LocationDTO {
    Integer id;
    String name;
    Double latitude;
    Double longitude;

    public LocationDTO(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
}
