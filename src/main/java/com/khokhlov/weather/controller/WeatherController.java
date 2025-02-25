package com.khokhlov.weather.controller;

import com.khokhlov.weather.model.dto.LocationDTO;
import com.khokhlov.weather.model.dto.WeatherDTO;
import com.khokhlov.weather.model.entity.User;
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
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("sessionUser", user.getUsername());

        List<LocationDTO> locations = userService.getUserLocations(user.getId());

        List<WeatherDTO> weatherList = new ArrayList<>();
        for (LocationDTO location : locations) {
            WeatherDTO weatherDTO = weatherService.getWeatherByCoordinates(location.getLatitude(), location.getLongitude());
            weatherDTO.setId(location.getId());
            weatherDTO.setCityName(location.getName());
            weatherDTO.setDescription(toUpperCaseForFirstLetter(weatherDTO.getDescription()));
            weatherList.add(weatherDTO);
        }

        model.addAttribute("weatherList", weatherList);
        return "index";
    }

    private static String toUpperCaseForFirstLetter(String text) {
        StringBuilder builder = new StringBuilder(text);
        if (Character.isAlphabetic(text.codePointAt(0)))
            builder.setCharAt(0, Character.toUpperCase(text.charAt(0)));

        return builder.toString();
    }
}
