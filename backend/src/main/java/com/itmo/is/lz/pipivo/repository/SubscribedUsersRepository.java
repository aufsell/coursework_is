package com.itmo.is.lz.pipivo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubscribedUsersRepository {
    @PersistenceContext
    private EntityManager entityManager;

    // Получить список подписок для конкретного пользователя
    public List<Long> getFollowedUserIdsByUserId(Long userId) {
        return entityManager.createNativeQuery(
                        "SELECT followed_user FROM subscribed_users WHERE user_id = :userId"
                )
                .setParameter("userId", userId)
                .getResultList();
    }

    public Long getCountFollowedUserIdsByUserId(Long userId) {
        return (Long) entityManager.createNativeQuery(
                        "SELECT COUNT(followed_user) FROM subscribed_users WHERE user_id = :userId"
                )
                .setParameter("userId", userId)
                .getSingleResult();
    }

    @Transactional
    public void addFollowedUser(Long id, Long subscribleUserId) {
        entityManager.createNativeQuery(
                        "INSERT INTO subscribed_users (user_id, followed_user) VALUES (:userId, :followedUserId)"
                )
                .setParameter("userId", id)
                .setParameter("followedUserId", subscribleUserId)
                .executeUpdate();
    }

    public boolean existsFollowedUser(Long id, Long subscribleUserId) {
        Number count = (Number) entityManager.createNativeQuery(
                        "SELECT COUNT(*) FROM subscribed_users WHERE user_id = :userId AND followed_user = :followedUserId"
                )
                .setParameter("userId", id)
                .setParameter("followedUserId", subscribleUserId)
                .getSingleResult();

        return count.longValue() > 0;
    }

    @Transactional
    public void removeFollowedUser(Long id, Long subscribedUserId) {
        entityManager.createNativeQuery(
                        "DELETE FROM subscribed_users WHERE user_id = :userId AND followed_user = :followedUserId"
                )
                .setParameter("userId", id)
                .setParameter("followedUserId", subscribedUserId)
                .executeUpdate();
    }

    public List<Long> getFollowingUserIdsByUserId(Long userId) {
        return entityManager.createNativeQuery(
                        "SELECT user_id FROM subscribed_users WHERE followed_user = :userId"
                )
                .setParameter("userId", userId)
                .getResultList();
    }
}
