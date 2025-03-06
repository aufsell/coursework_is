package com.itmo.is.lz.pipivo.repository;

import com.itmo.is.lz.pipivo.dto.BeerDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecommendationRepository {
    @PersistenceContext
    private EntityManager entityManager;


    public List<Long> getRecomendatedBeersIdsByUserId(Long userId) {
        return entityManager.createNativeQuery(
                        "SELECT beer_id FROM recommendation  WHERE user_id = :userId"
                )
                .setParameter("userId", userId)
                .getResultList();
    }
}
