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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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
                .orElseThrow(() -> {
                    log.warn("Error when adding location: {}", locationCommand.name());
                    return new RuntimeException("username not found");
                });

        String locationName = locationCommand.name().trim();

        Location existingLocation = locationRepository.findByNameAndCoordinates(locationName, locationCommand.latitude(), locationCommand.longitude()).orElse(null);

        if (existingLocation != null && !user.getLocations().contains(existingLocation)) {
            user.getLocations().add(existingLocation);
            return;
        }

        if (existingLocation == null) {
            Location location = Location.builder()
                    .name(locationCommand.name())
                    .latitude(locationCommand.latitude())
                    .longitude(locationCommand.longitude())
                    .build();
            locationRepository.save(location);

            user.getLocations().add(location);
        }
    }

    @Transactional
    public void deleteLocation(User user, Integer locationId) {
        user = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> {
                    log.warn("Error when deleting location: {}", locationId);
                    return new RuntimeException("username not found");
                });

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
                    .name(locationResponse.locationName())
                    .state(locationResponse.state())
                    .country(locationResponse.country())
                    .latitude(locationResponse.lat())
                    .longitude(locationResponse.lon())
                    .build();

            locationList.add(location);
        }

        return locationList;
    }
}
