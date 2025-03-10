package com.itmo.is.lz.pipivo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationUserRepository {
    @PersistenceContext
    private EntityManager entityManager;


    public List<Long> getNotificationIdsByUserId(Long id) {
        return entityManager.createNativeQuery(
                "SELECT notification_id FROM notification_user WHERE user_id = :id"
                )
                .setParameter("id", id)
                .getResultList();
    }
}
