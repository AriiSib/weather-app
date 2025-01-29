package com.khokhlov.weather.mapper;

import com.khokhlov.weather.model.command.UserCommand;
import com.khokhlov.weather.model.dto.UserDTO;
import com.khokhlov.weather.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDTO toUserDTO(User user);

    User toUser(UserCommand userCommand);

}
