package com.itmo.is.lz.pipivo.repository;

import com.itmo.is.lz.pipivo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReviewsRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {

}
