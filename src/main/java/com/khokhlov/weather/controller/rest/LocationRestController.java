package com.khokhlov.weather.controller.rest;

import com.khokhlov.weather.model.command.LocationCommand;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/location")
@RequiredArgsConstructor
public class LocationRestController {

    private final LocationService locationService;

    @PostMapping("/add")
    public void addLocation(@RequestBody LocationCommand locationCommand, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        locationService.addLocation(user, locationCommand);
    }

}
