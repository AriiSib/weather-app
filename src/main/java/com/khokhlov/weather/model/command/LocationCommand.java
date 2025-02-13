package com.khokhlov.weather.model.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationCommand {
    private String name;
    private Double latitude;
    private Double longitude;
}
