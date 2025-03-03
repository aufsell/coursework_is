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

}
