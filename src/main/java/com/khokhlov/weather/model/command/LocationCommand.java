package com.khokhlov.weather.model.command;

import lombok.Data;

@Data
public class LocationCommand {
    String name;
    Double latitude;
    Double longitude;
}
