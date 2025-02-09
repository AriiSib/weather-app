package com.khokhlov.weather.controller;

import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
@RequiredArgsConstructor
public class WeatherController {

    private final LocationService locationService;

    @GetMapping
    public String getWeatherPage(Model model, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        if(user == null){
            return "redirect:/auth/login";
        }

        model.addAttribute("sessionUser", user.getUsername());
        model.addAttribute("locations", user.getLocations());
        return "index";
    }
}
