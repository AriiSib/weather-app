package com.khokhlov.weather.repository;

import com.khokhlov.weather.model.entity.Location;
import com.khokhlov.weather.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final SessionFactory sessionFactory;

    public Optional<User> findByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT u FROM User u LEFT JOIN FETCH u.locations WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public void registerUser(User user) {
        sessionFactory.getCurrentSession().persist(user);
    }

    public List<Location> findLocationsById(Integer userId) {
        return sessionFactory.getCurrentSession().createQuery("SELECT l FROM User u JOIN  u.locations l WHERE u.id = :userId", Location.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
