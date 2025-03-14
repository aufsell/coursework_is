package com.itmo.is.lz.pipivo.repository;

import com.itmo.is.lz.pipivo.dto.BeerDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FavouriteBeerRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Long> getFavouriteBeerIdsByUserId(Long userId) {
        return entityManager.createNativeQuery(
                        "SELECT beer_id FROM favourite_beer WHERE user_id = :userId"
                )
                .setParameter("userId", userId)
                .getResultList();
    }

    @Transactional
    public void removeBeerFromFavourites(Long userId, Long beerId) {
        entityManager.createNativeQuery(
                        "DELETE FROM favourite_beer WHERE user_id = :userId AND beer_id = :beerId"
                )
                .setParameter("userId", userId)
                .setParameter("beerId", beerId)
                .executeUpdate();
    }

    @Transactional
    public void addBeerToFavourites(Long userId, Long beerId) {
        entityManager.createNativeQuery(
                        "INSERT INTO favourite_beer (user_id, beer_id) VALUES (:userId, :beerId)"
                )
                .setParameter("userId", userId)
                .setParameter("beerId", beerId)
                .executeUpdate();
    }

    public boolean exists(Long userId, Long beerId) {
        Number count = (Number) entityManager.createNativeQuery(
                        "SELECT COUNT(*) FROM favourite_beer WHERE user_id = :userId AND beer_id = :beerId"
                )
                .setParameter("userId", userId)
                .setParameter("beerId", beerId)
                .getSingleResult();

        return count.longValue() > 0;
    }
}
