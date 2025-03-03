package com.itmo.is.lz.pipivo.service;

import com.itmo.is.lz.pipivo.dto.ReviewDTO;
import com.itmo.is.lz.pipivo.model.Beer;
import com.itmo.is.lz.pipivo.model.Review;
import com.itmo.is.lz.pipivo.model.User;
import com.itmo.is.lz.pipivo.repository.BeerRepository;
import com.itmo.is.lz.pipivo.repository.ReviewsRepository;
import com.itmo.is.lz.pipivo.repository.UserReviewsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewsService {
    private final UserService userService;
    private final ReviewsRepository reviewsRepository;
    private final BeerService beerService;
    private final BeerRepository beerRepository;
    private final ProfileService profileService;
    private final UserReviewsRepository userReviewsRepository;

    @Transactional
    public void addReview(Long beerId, ReviewDTO reviewDTO) {
        String userName = userService.getCurrentUsername();
        User user = userService.getByUsername(userName);

        Beer beer = beerRepository.findById(beerId)
                .orElseThrow(() -> new RuntimeException("Beer with id " + beerId + " was not found."));

        Review review = new Review();
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setBeer(beer);

        reviewsRepository.save(review);
        userReviewsRepository.addUserReview(user.getId(), review.getId());
    }
}
