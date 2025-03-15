package com.khokhlov.weather.model.dto;

import com.khokhlov.weather.model.entity.Location;

import java.util.List;

public record UserDTO(
        Integer id,
        String username,
        List<Location> locations
) {}
