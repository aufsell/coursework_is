package com.itmo.is.lz.pipivo.repository;

import com.itmo.is.lz.pipivo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewsRepository extends JpaRepository<Review, Long> {
}
