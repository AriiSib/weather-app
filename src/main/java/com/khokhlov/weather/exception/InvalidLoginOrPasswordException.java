package com.khokhlov.weather.exception;

import lombok.Getter;

public class InvalidLoginOrPasswordException extends RuntimeException {

    @Getter
    private final String ERROR_IDENTIFIER;

    public InvalidLoginOrPasswordException(String message) {
        super(message);
        ERROR_IDENTIFIER = "";
    }

    public InvalidLoginOrPasswordException(String message, String errorIdentifier) {
        super(message);
        ERROR_IDENTIFIER = errorIdentifier;
    }

}
