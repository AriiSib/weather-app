package com.khokhlov.weather.service;

import com.khokhlov.weather.consts.Consts;
import com.khokhlov.weather.model.apiweather.LocationResponse;
import com.khokhlov.weather.model.apiweather.WeatherResponse;
import com.khokhlov.weather.model.command.LocationCommand;
import com.khokhlov.weather.model.dto.LocationDTO;
import com.khokhlov.weather.model.entity.Location;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.repository.LocationRepository;
import com.khokhlov.weather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
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

        if (existingLocation.isEmpty()) {
            String url = String.format(Consts.WEATHER_FOR_LOCATION_URL, editedLocationName, API_KEY);
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

            if (response == null) {
                throw new RuntimeException("WeatherResponse is null");
            }

            Location location = Location.builder()
                    .name(response.getLocationName().toLowerCase())
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
        locationRepository.deleteLocation(locationToDelete);

        user.getLocations().remove(locationToDelete);
    }

    public List<LocationDTO> findLocation(String locationName) {
        String editedLocationName = locationName.toLowerCase().trim();
        String url = String.format(Consts.LOCATION_SEARCH_URL, editedLocationName, API_KEY);

        LocationResponse[] response = restTemplate.getForObject(url, LocationResponse[].class);

        if (response == null) {
            throw new RuntimeException("LocationResponse is null for  " + editedLocationName);
        }

        List<LocationDTO> locationList = new ArrayList<>();
        for (LocationResponse locationResponse : response) {
            LocationDTO location = LocationDTO.builder()
                    .locationName(locationResponse.getLocationName())
                    .state(locationResponse.getState())
                    .country(locationResponse.getCountry())
                    .latitude(locationResponse.getLat())
                    .longitude(locationResponse.getLon())
                    .build();

            locationList.add(location);
        }

        return locationList;
    }
}
