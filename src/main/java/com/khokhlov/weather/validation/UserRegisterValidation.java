package com.khokhlov.weather.validation;

import com.khokhlov.weather.exception.InvalidLoginOrPasswordException;
import com.khokhlov.weather.model.command.UserRegisterCommand;
import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class UserRegisterValidation {

    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]+$";
    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9#*!]+$";

    public static void validate(UserRegisterCommand data) throws InvalidLoginOrPasswordException {
        validateUsernamePattern(data.getUsername());
        validateUsernameLength(data.getUsername());
        validatePasswordPattern(data.getPassword());
        validatePasswordLength(data.getPassword());
        validateMatchPasswords(data.getPassword(), data.getRepeatPassword());
    }

    private static void validateUsernamePattern(String username) {
        if (!Pattern.matches(USERNAME_PATTERN, username)) {
            throw new InvalidLoginOrPasswordException("Username should contain only letters and numbers", "usernameError");
        }
    }

    private static void validateUsernameLength(String username) {
        if (username.length() < 4) {
            throw new InvalidLoginOrPasswordException("Username must be at least 4 characters long", "usernameError");
        } else if (username.length() > 32) {
            throw new InvalidLoginOrPasswordException("Username must be at most 32 characters", "usernameError");
        }
    }

    private static void validatePasswordPattern(String password) {
        if (!Pattern.matches(PASSWORD_PATTERN, password)) {
            throw new InvalidLoginOrPasswordException("Password should contain only letters and numbers and !, #, *", "passwordError");
        }
    }

    private static void validatePasswordLength(String password) {
        if (password.length() < 6) {
            throw new InvalidLoginOrPasswordException("Password must be at least 6 characters", "passwordError");
        } else if (password.length() > 32) {
            throw new InvalidLoginOrPasswordException("Password must be at most 32 characters", "passwordError");
        }
    }

    private static void validateMatchPasswords(String password, String repeatPassword) {
        if (!password.equals(repeatPassword)) {
            throw new InvalidLoginOrPasswordException("Passwords don't match", "passwordError");
        }
    }

}
