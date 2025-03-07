package com.itmo.is.lz.pipivo.repository;

import com.itmo.is.lz.pipivo.model.User;
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

    public User getUserByReviewId(Long id) {
        return (User) entityManager.createNativeQuery("SELECT u.* FROM review_user ru JOIN users u ON ru.user_id = u.id WHERE ru.review_id = :review_id", User.class)
                .setParameter("review_id", id)
                .getSingleResult();
    }


    @Transactional
    public void deleteUserReviewByReviewId(Long reviewId) {
        entityManager.createNativeQuery("DELETE FROM review_user WHERE review_id = :review_id")
                .setParameter("review_id", reviewId)
                .executeUpdate();
    }
}
