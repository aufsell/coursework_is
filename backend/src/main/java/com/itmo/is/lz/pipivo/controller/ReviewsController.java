package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.ReviewDTO;
import com.itmo.is.lz.pipivo.service.ReviewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ReviewsController {
    private final ReviewsService reviewsService;

    public ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    @PostMapping("/beers/{beerId}/reviews")
    public ResponseEntity addReview(@PathVariable Long beerId, @RequestBody ReviewDTO reviewDTO) {
        reviewsService.addReview(beerId, reviewDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reviews/beer/{beerId}")
    public ResponseEntity getReviewsByBeerId(@PathVariable Long beerId, Pageable pageable, @RequestParam Map<String, String> filters) {
        Page<ReviewDTO> reviews = reviewsService.getReviewsByBeerId(beerId, pageable, filters);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reviews/user/{userId}")
    public ResponseEntity getReviewsByUserId(@PathVariable Long userId, Pageable pageable, @RequestParam Map<String, String> filters) {
        Page<ReviewDTO> reviews = reviewsService.getReviewsByUserId(userId, pageable, filters);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reviews/user/{userId}/count")
    public ResponseEntity getReviewsCountByUserId(@PathVariable Long userId) {
        Long reviewsCount = reviewsService.getReviewsCountByUserId(userId);
        return ResponseEntity.ok(reviewsCount);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity updateReview(@PathVariable Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        reviewsService.updateReview(reviewId, reviewDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity deleteReview(@PathVariable Long reviewId) {
        reviewsService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

}
