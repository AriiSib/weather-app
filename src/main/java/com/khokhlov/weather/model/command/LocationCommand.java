package com.khokhlov.weather.model.command;

public record LocationCommand(
        String name,
        Double latitude,
        Double longitude
) {}
