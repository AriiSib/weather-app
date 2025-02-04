package com.khokhlov.weather.service;

import com.khokhlov.weather.model.dto.UserDTO;
import com.khokhlov.weather.model.entity.Session;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.repository.SessionRepository;
import com.khokhlov.weather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Transactional
    public String createSession(String username) {
        //todo: userDTO -> userId?
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Session session = Session.builder()
                .id(UUID.randomUUID())
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();

        sessionRepository.save(session);
        return session.getId().toString();
    }
}
