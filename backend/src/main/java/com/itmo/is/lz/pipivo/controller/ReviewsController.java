package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.ReviewDTO;
import com.itmo.is.lz.pipivo.service.ReviewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewsController {
    private final ReviewsService reviewsService;

    public ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    @PostMapping("/{beerId}")
    public ResponseEntity addReview(@PathVariable Long beerId, @RequestBody ReviewDTO reviewDTO) {
        reviewsService.addReview(beerId, reviewDTO);
        return ResponseEntity.ok().build();
    }

}
