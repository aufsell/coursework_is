package com.itmo.is.lz.pipivo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Получить все notification_id для пользователя
    public List<Long> getNotificationIdsByUserId(Long userId) {
        return entityManager.createNativeQuery(
                        "SELECT notification_id FROM notification_user WHERE user_id = :userId"
                )
                .setParameter("userId", userId)
                .getResultList();
    }

    // Добавить связь между пользователем и уведомлением
    @Transactional
    public void addNotificationForUser(Long userId, Long notificationId) {
        entityManager.createNativeQuery(
                        "INSERT INTO notification_user (user_id, notification_id) VALUES (:userId, :notificationId)"
                )
                .setParameter("userId", userId)
                .setParameter("notificationId", notificationId)
                .executeUpdate();
    }

    // Удалить связь между пользователем и уведомлением
    @Transactional
    public void removeNotificationForUser(Long userId, Long notificationId) {
        entityManager.createNativeQuery(
                        "DELETE FROM notification_user WHERE user_id = :userId AND notification_id = :notificationId"
                )
                .setParameter("userId", userId)
                .setParameter("notificationId", notificationId)
                .executeUpdate();
    }

    // Проверить существование связи между пользователем и уведомлением
    public boolean exists(Long userId, Long notificationId) {
        Number count = (Number) entityManager.createNativeQuery(
                        "SELECT COUNT(*) FROM notification_user WHERE user_id = :userId AND notification_id = :notificationId"
                )
                .setParameter("userId", userId)
                .setParameter("notificationId", notificationId)
                .getSingleResult();

        return count.longValue() > 0;
    }

    public List<Long> findByUserId(Long userId) {
        return entityManager.createNativeQuery(
                "SELECT notification_id FROM notification_user WHERE user_id = :userId")
                .setParameter("userId", userId)
                .getResultList();
    }
}
