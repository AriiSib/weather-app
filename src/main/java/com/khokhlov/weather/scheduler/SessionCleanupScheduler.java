package com.khokhlov.weather.scheduler;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SessionCleanupScheduler {

    private final SessionFactory sessionFactory;

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanupSession() {
        sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM Session s WHERE s.expiresAt <= current_timestamp ")
                .executeUpdate();
    }
}
