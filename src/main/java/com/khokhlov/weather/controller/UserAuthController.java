package com.khokhlov.weather.controller;

import com.khokhlov.weather.mapper.UserMapper;
import com.khokhlov.weather.model.command.UserCommand;
import com.khokhlov.weather.model.dto.UserDTO;
import com.khokhlov.weather.service.SessionService;
import com.khokhlov.weather.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserService userService;
    private final SessionService sessionService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public UserDTO loginUser(@RequestBody UserCommand userCommand, HttpServletResponse response) {
        UserDTO userDTO = userService.loginUser(userCommand);
        String sessionID = sessionService.createSession(userDTO.getUsername());

        Cookie cookie = new Cookie("SESSION_ID", sessionID);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        return userDTO;
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody UserCommand userCommand) {
        userService.registerUser(userCommand);
    }
}
