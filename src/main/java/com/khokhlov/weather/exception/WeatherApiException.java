package com.khokhlov.weather.exception;

import lombok.Getter;

public class WeatherApiException extends RuntimeException {

    @Getter
    private final String ERROR_IDENTIFIER;

    public WeatherApiException(String message) {
        super(message);
        ERROR_IDENTIFIER = "";
    }

    public WeatherApiException(String message, String errorIdentifier) {
        super(message);
        ERROR_IDENTIFIER = errorIdentifier;
    }
}
