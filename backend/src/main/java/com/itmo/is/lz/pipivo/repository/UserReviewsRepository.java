package com.itmo.is.lz.pipivo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class UserReviewsRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void addUserReview(Long userId, Long reviewId) {
        entityManager.createNativeQuery("INSERT INTO review_user (user_id, review_id) VALUES (:user_id, :review_id)")
                .setParameter("user_id", userId)
                .setParameter("review_id", reviewId)
                .executeUpdate();
    }
}
