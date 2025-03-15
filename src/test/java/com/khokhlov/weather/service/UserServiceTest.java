package com.khokhlov.weather.service;

import com.khokhlov.weather.config.ApplicationConfig;
import com.khokhlov.weather.exception.InvalidLoginOrPasswordException;
import com.khokhlov.weather.model.command.UserCommand;
import com.khokhlov.weather.model.command.UserRegisterCommand;
import com.khokhlov.weather.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "test")
@TestPropertySource(properties = "spring.profiles.active=test")
@ContextConfiguration(classes = {ApplicationConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    SessionService sessionService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    @Test
    void Should_RegisterUser_When_DataIsValid() {
        UserRegisterCommand userRegisterCommand = new UserRegisterCommand("testUser", "testPassword", "testPassword");
        UserCommand userCommand = new UserCommand("testUser", "testPassword");

        userService.registerUser(userRegisterCommand);
        User user = userService.loginUser(userCommand);

        assertEquals("testUser".toLowerCase(), user.getUsername());
        assertTrue(passwordEncoder.matches("testPassword", user.getPassword()));
    }

    @Test
    void Should_ThrowRegisterException_When_DataIsNotValid() {
        UserRegisterCommand userInvalidCommand = new UserRegisterCommand("@#$", "@#$", "@#$");

        assertThrows(InvalidLoginOrPasswordException.class, () -> userService.registerUser(userInvalidCommand));
    }

    @Test
    void Should_ThrowRegisterException_When_UserAlreadyExists() {
        UserRegisterCommand userRegisterCommand = new UserRegisterCommand("testUser", "testPassword", "testPassword");
        UserRegisterCommand userRegisterCommand1 = new UserRegisterCommand("testUser", "testPassword", "testPassword");

        userService.registerUser(userRegisterCommand);

        InvalidLoginOrPasswordException exception =
                assertThrows(InvalidLoginOrPasswordException.class, () -> userService.registerUser(userRegisterCommand1));

        assertEquals("Account with this username already exists.", exception.getMessage());
    }

    @Test
    void Should_LoginAndCreateSession_When_UserIsValid() {
        UserRegisterCommand userRegisterCommand = new UserRegisterCommand("testUser", "testPassword", "testPassword");
        UserCommand userCommand = new UserCommand("testUser", "testPassword");

        userService.registerUser(userRegisterCommand);
        User user = userService.loginUser(userCommand);
        String session = sessionService.createSession(user);

        assertNotNull(user);
        assertNotNull(session);
        assertEquals(userCommand.username().toLowerCase(), user.getUsername());
        assertEquals(session, sessionService.findSessionById(UUID.fromString(session)).orElseThrow().getId().toString());
    }

    @Test
    void Should_ThrowLoginException_When_PasswordIsWrong() {
        UserRegisterCommand userRegisterCommand = new UserRegisterCommand("testUser", "testPassword", "testPassword");
        UserCommand userInvalidCommand = new UserCommand("testUser", "InvalidPassword");

        userService.registerUser(userRegisterCommand);

        InvalidLoginOrPasswordException exception =
                assertThrows(InvalidLoginOrPasswordException.class, () -> userService.loginUser(userInvalidCommand));

        assertEquals("Wrong password", exception.getMessage());
    }

    @Test
    void Should_ThrowLoginException_When_UserIsNotExist() {
        UserCommand userInvalidCommand = new UserCommand("InvalidUsername", "testPassword");

        InvalidLoginOrPasswordException exception =
                assertThrows(InvalidLoginOrPasswordException.class, () -> userService.loginUser(userInvalidCommand));

        assertEquals("User with username \"" + userInvalidCommand.username() + "\" does not exist", exception.getMessage());
    }
}
