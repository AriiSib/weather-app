package com.khokhlov.weather.model.command;

public record UserCommand(
        String username,
        String password
) {}
