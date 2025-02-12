package com.khokhlov.weather.controller;

import com.khokhlov.weather.mapper.UserMapper;
import com.khokhlov.weather.model.command.UserCommand;
import com.khokhlov.weather.model.dto.UserDTO;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.service.SessionService;
import com.khokhlov.weather.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserService userService;
    private final SessionService sessionService;
    private final UserMapper userMapper;

    @GetMapping("/login")
    public String showLoginPage() {
        return "sign-in";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpServletResponse response) {
        User user = userService.loginUser(new UserCommand(username, password));
        String sessionID = sessionService.createSession(user);

        Cookie cookie = new Cookie("SESSION_ID", sessionID);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        return "redirect:/index";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "sign-up";
    }


    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password) {
        userService.registerUser(new UserCommand(username, password));
        return "redirect:/auth/login";
    }
}

//todo: LocationController(MVC),logout, delete session from DB