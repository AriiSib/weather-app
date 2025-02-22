package com.khokhlov.weather.controller.rest;

import com.khokhlov.weather.mapper.UserMapper;
import com.khokhlov.weather.model.command.UserCommand;
import com.khokhlov.weather.model.command.UserRegisterCommand;
import com.khokhlov.weather.model.dto.UserDTO;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.service.SessionService;
import com.khokhlov.weather.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rest/auth")
@RequiredArgsConstructor
public class UserRestAuthController {

    private final UserService userService;
    private final SessionService sessionService;
    private final UserMapper userMapper;


    @PostMapping("/login")
    public UserDTO loginUser(@RequestBody UserCommand userCommand,
                             HttpServletResponse response) {
        User user = userService.loginUser(userCommand);
        String sessionID = sessionService.createSession(user);

        Cookie cookie = new Cookie("SESSION_ID", sessionID);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        return userMapper.toUserDTO(user);
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody UserRegisterCommand userRegisterCommand, Model model) {
        userService.registerUser(userRegisterCommand);
    }
}
