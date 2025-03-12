package com.khokhlov.weather.controller;

import com.khokhlov.weather.exception.InvalidLoginOrPasswordException;
import com.khokhlov.weather.model.command.UserCommand;
import com.khokhlov.weather.model.command.UserRegisterCommand;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.service.SessionService;
import com.khokhlov.weather.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserService userService;
    private final SessionService sessionService;

    @GetMapping("/login")
    public String showLoginPage() {
        log.debug("GET /auth/login - Show login page");
        return "sign-in";
    }

    @PostMapping("/login")
    public String loginUser(@CookieValue(value = "SESSION_ID", required = false) String sessionId,
                            @RequestParam("username") String username,
                            @RequestParam("password") String password,
                            HttpServletResponse response,
                            Model model) {
        log.info("POST /auth/login - Attempting to login user");

        User user = null;
        try {
            user = userService.loginUser(new UserCommand(username, password));
        } catch (InvalidLoginOrPasswordException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute(e.getERROR_IDENTIFIER(), e.getMessage());
            model.addAttribute("username", username);

            return "sign-in";
        }
        if (sessionId != null) {
            sessionService.deleteSession(sessionId);
        }
        createSessionAndCookie(response, user);

        log.info("POST /auth/login - User: {} successfully logged", user.getUsername());
        return "redirect:/index";
    }

    @PostMapping("/logout")
    public String logoutUser(@CookieValue(value = "SESSION_ID", required = false) String sessionID,
                             HttpServletResponse response) {
        if (sessionID != null) {
            sessionService.deleteSession(sessionID);
        }

        deleteCookie(response);

        log.debug("GET /auth/logout - User logged out with SESSION_ID: {}", sessionID);
        return "redirect:/auth/login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        log.debug("GET /auth/register - Show register page");
        return "sign-up";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("repeat-password") String repeatPassword,
                               Model model) {
        log.info("POST /auth/register - Attempting to register user");

        try {
            userService.registerUser(new UserRegisterCommand(username, password, repeatPassword));
        } catch (InvalidLoginOrPasswordException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute(e.getERROR_IDENTIFIER(), e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            model.addAttribute("repeatPassword", repeatPassword);

            return "sign-up";
        }

        log.info("POST /auth/register - User: {} successfully registered", username);
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
