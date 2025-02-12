package com.khokhlov.weather.controller;

import com.khokhlov.weather.model.dto.WeatherDTO;
import com.khokhlov.weather.model.entity.Location;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.service.LocationService;
import com.khokhlov.weather.service.UserService;
import com.khokhlov.weather.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/index")
@RequiredArgsConstructor
public class WeatherController {

    private final UserService userService;
    private final WeatherService weatherService;

    @GetMapping
    public String getWeatherPage(Model model, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("sessionUser", user.getUsername());

        List<Location> locations = userService.getUserLocations(user.getId());

        List<WeatherDTO> weatherList = new ArrayList<>();
        for (Location location : locations) {
            WeatherDTO weatherDTO = weatherService.getWeatherByCoordinate(location.getLatitude(), location.getLongitude());
            weatherList.add(weatherDTO);
        }


        model.addAttribute("weatherList ", weatherList);
        return "index";
    }
}
