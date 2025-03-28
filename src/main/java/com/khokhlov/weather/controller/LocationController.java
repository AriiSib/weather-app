package com.khokhlov.weather.controller;

import com.khokhlov.weather.model.command.LocationCommand;
import com.khokhlov.weather.model.dto.LocationDTO;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/add")
    public String addLocation(@RequestParam("name") String name,
                              @RequestParam(value = "latitude", required = false) Double latitude,
                              @RequestParam(value = "longitude", required = false) Double longitude,
                              HttpServletRequest request) {
        log.info("POST /location - Attempting to add location for user: {}", name);

        User user = (User) request.getSession().getAttribute("user");
        locationService.addLocation(user, new LocationCommand(name, latitude, longitude));

        log.info("POST /location - Successfully added location for user: {}", name);
        return "redirect:/index";
    }

    @PostMapping("/search")
    public String searchLocation(@RequestParam("name") String name,
                                 Model model) {
        List<LocationDTO> locations = locationService.findLocation(name);

        model.addAttribute("locationList", locations);

        log.debug("POST /location/search - Successfully found location for user: {}", name);
        return "search-results";
    }

    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("id") Integer id,
                                 HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        locationService.deleteLocation(user, id);

        log.info("POST /location/delete - Successfully deleted location for user: {}", id);
        return "redirect:/index";
    }

}
