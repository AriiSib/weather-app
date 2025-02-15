package com.khokhlov.weather.service;

import com.khokhlov.weather.consts.Consts;
import com.khokhlov.weather.model.apiweather.OpenWeatherResponse;
import com.khokhlov.weather.model.command.LocationCommand;
import com.khokhlov.weather.model.entity.Location;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.repository.LocationRepository;
import com.khokhlov.weather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String API_KEY;

    @Transactional
    public void addLocation(User user, LocationCommand locationCommand) {
        user = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("username not found"));

        String editedLocationName = locationCommand.getName().toLowerCase().trim();

        Optional<Location> existingLocation = locationRepository.findByName(editedLocationName);

        Location location;
        if (existingLocation.isPresent()) {
            location = existingLocation.get();
        } else {
            String url = String.format(Consts.GET_LOCATION_URL, editedLocationName, API_KEY);
            OpenWeatherResponse response = restTemplate.getForObject(url, OpenWeatherResponse.class);

            if (response == null) {
                throw new RuntimeException("OpenWeatherResponse is null");
            }

            location = Location.builder()
                    .name(response.getName())
                    .latitude(response.getCoord().getLat())
                    .longitude(response.getCoord().getLon())
                    .build();
            locationRepository.save(location);

            if (!user.getLocations().contains(location)) {
                user.getLocations().add(location);
            }
        }
    }

    @Transactional
    public void deleteLocation(User user, Integer locationId) {
        user = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("username not found"));

        Location locationToDelete = locationRepository.findById(locationId);

        user.getLocations().remove(locationToDelete);
    }
}
