package com.khokhlov.weather.service;

import com.khokhlov.weather.consts.Consts;
import com.khokhlov.weather.model.apiweather.LocationResponse;
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

        String locationName = locationCommand.getName().trim();

        Location existingLocation = locationRepository.findByNameAndCoordinates(locationName, locationCommand.getLatitude(), locationCommand.getLongitude()).orElse(null);

        if (existingLocation != null && !user.getLocations().contains(existingLocation)) {
            user.getLocations().add(existingLocation);
            return;
        }

        if (existingLocation == null) {
            Location location = Location.builder()
                    .name(locationCommand.getName())
                    .latitude(locationCommand.getLatitude())
                    .longitude(locationCommand.getLongitude())
                    .build();
            locationRepository.save(location);

            user.getLocations().add(location);
        }
    }

    @Transactional
    public void deleteLocation(User user, Integer locationId) {
        user = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("username not found"));

        Location locationToDelete = locationRepository.findById(locationId);
        user.getLocations().remove(locationToDelete);
    }

    public List<LocationDTO> findLocation(String locationName) {
        locationName = locationName.toLowerCase().trim();
        String url = String.format(Consts.LOCATION_SEARCH_URL, locationName, API_KEY);

        LocationResponse[] response = restTemplate.getForObject(url, LocationResponse[].class);

        List<LocationDTO> locationList = new ArrayList<>();
        for (LocationResponse locationResponse : response) {
            LocationDTO location = LocationDTO.builder()
                    .name(locationResponse.getLocationName())
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
