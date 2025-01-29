package com.khokhlov.weather.repository;

import com.khokhlov.weather.model.command.UserCommand;
import com.khokhlov.weather.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final SessionFactory sessionFactory;

    public Optional<User> findByUsername(UserCommand command) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", command.getUsername())
                .uniqueResultOptional();
    }

    public void registerUser(User user) {
        sessionFactory.getCurrentSession().persist(user);
    }
}
