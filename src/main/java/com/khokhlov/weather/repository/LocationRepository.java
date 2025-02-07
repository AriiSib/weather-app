package com.khokhlov.weather.repository;

import com.khokhlov.weather.model.entity.Location;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LocationRepository {

    private final SessionFactory sessionFactory;

    public List<Location> findByUserId(Long userId) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Location WHERE user.id = :userId", Location.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
