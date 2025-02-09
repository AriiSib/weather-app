package com.khokhlov.weather.repository;

import com.khokhlov.weather.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final SessionFactory sessionFactory;

    public Optional<User> findByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public Optional<User> findByIdWithLocations(Integer userId) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT u FROM User u LEFT JOIN FETCH u.locations WHERE u.id = :userId", User.class)
                .setParameter("userId", userId)
                .uniqueResultOptional();
    }

    public void registerUser(User user) {
        sessionFactory.getCurrentSession().persist(user);
    }
}
