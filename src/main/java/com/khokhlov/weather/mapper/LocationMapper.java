package com.khokhlov.weather.mapper;

import com.khokhlov.weather.model.dto.LocationDTO;
import com.khokhlov.weather.model.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {

    LocationDTO toLocationDTO(Location location);
}
