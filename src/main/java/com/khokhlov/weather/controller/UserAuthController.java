package com.khokhlov.weather.controller;

import com.khokhlov.weather.model.command.UserCommand;
import com.khokhlov.weather.model.dto.UserDTO;
import com.khokhlov.weather.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody UserCommand userCommand) {
        return ResponseEntity.ok(userService.loginUser(userCommand));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserCommand userCommand) {
        userService.registerUser(userCommand);
        return ResponseEntity.ok().build();
    }
}
