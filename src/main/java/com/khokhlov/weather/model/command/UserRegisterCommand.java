package com.khokhlov.weather.model.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterCommand {
    private String username;
    private String password;
    private String repeatPassword;
}
