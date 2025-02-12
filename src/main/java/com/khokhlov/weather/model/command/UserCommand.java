package com.khokhlov.weather.model.command;

import lombok.Data;

@Data
public class UserCommand {
    private final String username;
    private final String password;
}
