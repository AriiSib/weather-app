package com.khokhlov.weather.service;

import com.khokhlov.weather.model.entity.Session;
import com.khokhlov.weather.model.entity.User;
import com.khokhlov.weather.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    @Transactional
    public String createSession(User user) {
        Session session = Session.builder()
                .id(UUID.randomUUID())
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(12))
                .build();

        sessionRepository.save(session);
        return session.getId().toString();
    }

    @Transactional
    public Optional<Session> findSessionById(UUID sessionId) {
        return sessionRepository.findById(sessionId);
    }

    @Transactional
    public void deleteSession(String sessionID) {
        try {
            sessionRepository.deleteSession(UUID.fromString(sessionID));
        } catch (NoSuchElementException e) {
            return;
        }
    }
}
