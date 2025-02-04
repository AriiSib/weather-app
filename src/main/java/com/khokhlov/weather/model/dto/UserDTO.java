package com.khokhlov.weather.model.dto;

import com.khokhlov.weather.model.entity.Location;
import lombok.Value;

import java.util.List;

@Value
public class UserDTO {
    long id;
    String username;
    List<Location> locations;
}
