package com.khokhlov.weather.service;

import com.khokhlov.weather.exception.InvalidLoginException;
import com.khokhlov.weather.exception.InvalidPasswordException;
import com.khokhlov.weather.mapper.UserMapper;
import com.khokhlov.weather.model.command.UserCommand;
import com.khokhlov.weather.model.dto.UserDTO;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserDTO loginUser(UserCommand userCommand) {
        User user = userRepository.findByUsername(userCommand)
                .orElseThrow(() -> new InvalidLoginException("User with username " + userCommand.getUsername() + " does not exist"));

        if (!user.getPassword().equals(userCommand.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        return userMapper.toUserDTO(user);
    }

    @Transactional
    public void registerUser(UserCommand userCommand) {
        User userToSave = userMapper.toUser(userCommand);
        userRepository.registerUser(userToSave);
    }

}
