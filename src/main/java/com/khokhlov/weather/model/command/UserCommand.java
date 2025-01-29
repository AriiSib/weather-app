package com.khokhlov.weather.model.command;

import lombok.Data;

@Data
public class UserCommand {
    private String username;
    private String password;
}
