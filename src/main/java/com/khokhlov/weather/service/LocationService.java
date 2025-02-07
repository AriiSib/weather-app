package com.khokhlov.weather.service;

import com.khokhlov.weather.model.dto.LocationDTO;
import com.khokhlov.weather.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public List<LocationDTO> getUserLocations(Long  userId) {
        return locationRepository.findByUserId(userId);
    }
}
