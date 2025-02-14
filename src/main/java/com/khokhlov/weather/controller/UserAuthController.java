package com.khokhlov.weather.controller;

import com.khokhlov.weather.mapper.UserMapper;
import com.khokhlov.weather.model.command.UserCommand;
import com.khokhlov.weather.model.entity.Session;
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

    @GetMapping("/login")
    public String showLoginPage() {
        return "sign-in";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            HttpServletResponse response) {
        User user = userService.loginUser(new UserCommand(username, password));
        createSessionAndCookie(response, user);

        return "redirect:/index";
    }

    @PostMapping("/logout")
    public String logoutUser(@CookieValue(value = "SESSION_ID", required = false) String sessionID,
                             HttpServletResponse response) {
        if (sessionID != null) {
            sessionService.deleteSession(sessionID);
        }

        deleteCookie(response);
        return "redirect:/auth/login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "sign-up";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password) {
        userService.registerUser(new UserCommand(username, password));
        return "redirect:/auth/login";
    }

    private void createSessionAndCookie(HttpServletResponse response, User user) {
        String sessionID = sessionService.createSession(user);

        Cookie cookie = new Cookie("SESSION_ID", sessionID);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }

    private void deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("SESSION_ID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}

//todo: LocationController(MVC),logout, delete session from DB