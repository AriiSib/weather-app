package com.khokhlov.weather.error.rest;

public record AppError(
        int statusCode,
        String message
) {}
