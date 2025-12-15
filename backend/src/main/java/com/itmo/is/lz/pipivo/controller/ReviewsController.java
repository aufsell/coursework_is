package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.ReviewDTO;
import com.itmo.is.lz.pipivo.service.ReviewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ReviewsController {
    private final ReviewsService reviewsService;

    public ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    @PostMapping("/beers/{beerId}/reviews")
    public ResponseEntity<Void> addReview(@PathVariable Long beerId,
                                          @RequestBody ReviewDTO reviewDTO) {

        log.info("Add review for beer. beerId={}", beerId);

        reviewsService.addReview(beerId, reviewDTO);

        log.debug("Review added successfully. beerId={}", beerId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/reviews/beer/{beerId}")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByBeerId(
            @PathVariable Long beerId,
            Pageable pageable,
            @RequestParam Map<String, String> filters) {
        log.info("Get reviews by beer. beerId={}, page={}, size={}",
                beerId, pageable.getPageNumber(), pageable.getPageSize());
        Page<ReviewDTO> reviews = reviewsService.getReviewsByBeerId(beerId, pageable, filters);
        log.debug("Reviews fetched by beer. beerId={}, count={}",
                beerId, reviews.getNumberOfElements());
        return ResponseEntity.ok(reviews);
    }


    @GetMapping("/reviews/user/{userId}")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByUserId(
            @PathVariable Long userId,
            Pageable pageable,
            @RequestParam Map<String, String> filters) {
        log.info("Get reviews by user. userId={}, page={}, size={}",
                userId, pageable.getPageNumber(), pageable.getPageSize());
        Page<ReviewDTO> reviews = reviewsService.getReviewsByUserId(userId, pageable, filters);
        log.debug("Reviews fetched by user. userId={}, count={}",
                userId, reviews.getNumberOfElements());
        return ResponseEntity.ok(reviews);
    }


    @GetMapping("/reviews/user/{userId}/count")
    public ResponseEntity<Long> getReviewsCountByUserId(@PathVariable Long userId) {
        log.info("Get reviews count by user. userId={}", userId);
        Long reviewsCount = reviewsService.getReviewsCountByUserId(userId);
        log.debug("Reviews count fetched. userId={}, count={}", userId, reviewsCount);
        return ResponseEntity.ok(reviewsCount);
    }


    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable Long reviewId,
                                             @RequestBody ReviewDTO reviewDTO) {
        log.info("Update review. reviewId={}", reviewId);
        reviewsService.updateReview(reviewId, reviewDTO);
        log.debug("Review updated successfully. reviewId={}", reviewId);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        log.info("Delete review. reviewId={}", reviewId);
        reviewsService.deleteReview(reviewId);
        log.debug("Review deleted successfully. reviewId={}", reviewId);
        return ResponseEntity.ok().build();
    }


}
