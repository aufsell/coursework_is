package com.itmo.is.lz.pipivo.repository;

import com.itmo.is.lz.pipivo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {

    @Query(value = "SELECT * FROM get_top_beers()", nativeQuery = true)
    List<Object[]> findTopBeers();

}
