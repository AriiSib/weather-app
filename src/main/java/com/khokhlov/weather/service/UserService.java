package com.khokhlov.weather.service;

import com.khokhlov.weather.exception.InvalidLoginOrPasswordException;
import com.khokhlov.weather.mapper.LocationMapper;
import com.khokhlov.weather.mapper.UserMapper;
import com.khokhlov.weather.model.command.UserCommand;
import com.khokhlov.weather.model.command.UserRegisterCommand;
import com.khokhlov.weather.model.dto.LocationDTO;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.repository.UserRepository;
import com.khokhlov.weather.validation.UserRegisterValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LocationMapper locationMapper;


    @Transactional(readOnly = true)
    public User loginUser(UserCommand userCommand) {
        User user = userRepository.findByUsername(userCommand.getUsername().toLowerCase())
                .orElseThrow(() -> new InvalidLoginOrPasswordException("User with username \"" + userCommand.getUsername() + "\" does not exist", "usernameError"));

        if (!passwordEncoder.matches(userCommand.getPassword(), user.getPassword())) {
            throw new InvalidLoginOrPasswordException("Wrong password", "passwordError");
        }

        return user;
    }

    @Transactional
    public void registerUser(UserRegisterCommand userRegisterCommand) {
        UserRegisterValidation.validate(userRegisterCommand);

        userRegisterCommand.setUsername(userRegisterCommand.getUsername().toLowerCase());

        User userToSave = userMapper.toUser(userRegisterCommand);
        userToSave.setPassword(passwordEncoder.encode(userRegisterCommand.getPassword()));
        userRepository.registerUser(userToSave);
    }

    @Transactional(readOnly = true)
    public List<LocationDTO> getUserLocations(Integer userId) {
        return userRepository.findLocationsById(userId).stream()
                .map(locationMapper::toLocationDTO)
                .toList();
    }

}
