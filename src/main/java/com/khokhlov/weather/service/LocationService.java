package com.khokhlov.weather.service;

import com.khokhlov.weather.model.command.LocationCommand;
import com.khokhlov.weather.model.dto.LocationDTO;
import com.khokhlov.weather.model.entity.Location;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.repository.LocationRepository;
import com.khokhlov.weather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

//    @Transactional(readOnly = true)
//    public List<LocationDTO> getUserLocations(Integer userId) {
//        return locationRepository.findByUserId(userId).stream()
//                .map(LocationDTO::new)
//                .toList();
//    }

    @Transactional
    public void addLocation(User user, LocationCommand locationCommand) {
        user = userRepository.findByIdWithLocations(user.getId()).get();

        Optional<Location> existingLocation = locationRepository.findByName(locationCommand.getName());

        Location location;
        if (existingLocation.isPresent()) {
            location = existingLocation.get();
        } else {
            location = Location.builder()
                    .name(locationCommand.getName())
                    .latitude(locationCommand.getLatitude())
                    .longitude(locationCommand.getLongitude())
                    .build();
            locationRepository.save(location);

            if (!user.getLocations().contains(location)) {
                user.getLocations().add(location);
            }
        }
    }
}
