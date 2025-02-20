package com.khokhlov.weather.repository;

import com.khokhlov.weather.exception.InvalidLoginException;
import com.khokhlov.weather.model.entity.Location;
import com.khokhlov.weather.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
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
        try {
            sessionFactory.getCurrentSession().persist(user);
        } catch (ConstraintViolationException e) {
            throw new InvalidLoginException("Account with this username already exists.");
        }
    }

    public List<Location> findLocationsById(Integer userId) {
        return sessionFactory.getCurrentSession().createQuery("SELECT l FROM User u JOIN  u.locations l WHERE u.id = :userId", Location.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
