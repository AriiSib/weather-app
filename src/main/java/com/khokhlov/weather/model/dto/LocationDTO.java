package com.khokhlov.weather.model.dto;

import com.khokhlov.weather.model.entity.Location;
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
    String locationName;
    String country;
    String state;
    Double latitude;
    Double longitude;

    public LocationDTO(Location location) {
        this.id = location.getId();
        this.locationName = location.getName();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
}
