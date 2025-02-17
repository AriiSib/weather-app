package com.khokhlov.weather.repository;

import com.khokhlov.weather.model.entity.Location;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationRepository {

    private final SessionFactory sessionFactory;

//    public List<Location> findByUserId(Integer userId) {
//        return sessionFactory.getCurrentSession()
//                .createQuery("SELECT l FROM Location l JOIN l.users u WHERE u.id = :userId", Location.class)
//                .setParameter("userId", userId)
//                .getResultList();
//    }

    public Location findById(Integer locationId) {
        return sessionFactory.getCurrentSession().get(Location.class, locationId);
    }

    public Optional<Location> findByName(String locationName) {
        return sessionFactory.getCurrentSession().createQuery("SELECT l FROM Location  l WHERE l.name = :locationName", Location.class)
                .setParameter("locationName", locationName)
                .uniqueResultOptional();
    }

    public Optional<Location> findByNameAndCoordinates(String locationName, double latitude, double longitude) {
        return sessionFactory.getCurrentSession().createQuery("SELECT l FROM Location l WHERE l.name = :locationName AND l.latitude = :latitude AND l.longitude = :longitude", Location.class)
                .setParameter("locationName", locationName)
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .uniqueResultOptional();
    }

    public void save(Location location) {
        sessionFactory.getCurrentSession().persist(location);
    }

    public void deleteLocation(Location locationToDelete) {
        sessionFactory.getCurrentSession().remove(locationToDelete);
    }
}
