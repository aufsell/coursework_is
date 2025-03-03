package com.itmo.is.lz.pipivo.service;

import com.itmo.is.lz.pipivo.dto.ProfileDTO;
import com.itmo.is.lz.pipivo.dto.ReviewDTO;
import com.itmo.is.lz.pipivo.model.Beer;
import com.itmo.is.lz.pipivo.model.Review;
import com.itmo.is.lz.pipivo.model.User;
import com.itmo.is.lz.pipivo.repository.BeerRepository;
import com.itmo.is.lz.pipivo.repository.ReviewsRepository;
import com.itmo.is.lz.pipivo.repository.UserReviewsRepository;
import com.itmo.is.lz.pipivo.specification.ReviewsSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Map;

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

    public User getUser(Review review){
        User user =  userReviewsRepository.getUserByReviewId(review.getId());
        return user;
    }

    public Page<ReviewDTO> getReviewsByBeerId(Long beerId, Pageable pageable, Map<String, String> filters) {
        Specification<Review> spec = ReviewsSpecification.withFilters(filters, beerId, null);

        return reviewsRepository.findAll(spec, pageable).map(review ->
                new ReviewDTO(
                        review.getId(),
                        review.getRating(),
                        review.getComment(),
                        review.getBeer().getId(),
                        review.getBeer().getImagePath(),
                        review.getCreated_at().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        new ProfileDTO(getUser(review))
                )
        );
    }

    public Page<ReviewDTO> getReviewsByUserId(Long userId, Pageable pageable, Map<String, String> filters) {
        Specification<Review> spec = ReviewsSpecification.withFilters(filters, null, userId);

        return reviewsRepository.findAll(spec, pageable).map(review ->
                new ReviewDTO(
                        review.getId(),
                        review.getRating(),
                        review.getComment(),
                        review.getBeer().getId(),
                        review.getBeer().getImagePath(),
                        review.getCreated_at().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        new ProfileDTO(getUser(review))
                )
        );
    }
}
