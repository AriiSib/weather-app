package com.khokhlov.weather.repository;

import com.khokhlov.weather.model.entity.Session;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SessionRepository {

    private final SessionFactory sessionFactory;

    public void save(Session session) {
        sessionFactory.getCurrentSession().persist(session);
    }

    public Optional<Session> findById(UUID sessionId) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Session.class, sessionId));
    }
}
