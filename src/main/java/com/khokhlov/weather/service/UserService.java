package com.khokhlov.weather.service;

import com.khokhlov.weather.exception.InvalidLoginException;
import com.khokhlov.weather.exception.InvalidPasswordException;
import com.khokhlov.weather.mapper.UserMapper;
import com.khokhlov.weather.model.command.UserCommand;
import com.khokhlov.weather.model.entity.Location;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.repository.UserRepository;
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


    @Transactional(readOnly = true)
    public User loginUser(UserCommand userCommand) {
        User user = userRepository.findByUsername(userCommand.getUsername())
                .orElseThrow(() -> new InvalidLoginException("User with username " + userCommand.getUsername() + " does not exist"));

        if (!passwordEncoder.matches(userCommand.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        return user;
    }

    @Transactional
    public void registerUser(UserCommand userCommand) {
        User userToSave = userMapper.toUser(userCommand);
        userToSave.setPassword(passwordEncoder.encode(userCommand.getPassword()));
        userRepository.registerUser(userToSave);
    }

    @Transactional(readOnly = true)
    public List<Location> getUserLocations(Integer userId) {
        return userRepository.findLocationsById(userId);

    }

}
