/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Rating;

import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sk.stu.fiit.projectBackend.Rating.dto.RatingRequest;
import sk.stu.fiit.projectBackend.Rating.dto.RatingResponse;
import sk.stu.fiit.projectBackend.TourOffer.TourOffer;
import sk.stu.fiit.projectBackend.TourOffer.TourOfferRepository;
import sk.stu.fiit.projectBackend.User.AppUser;
import sk.stu.fiit.projectBackend.Utils.AppUserUtils;
import sk.stu.fiit.projectBackend.exceptions.TourOfferNotFoundException;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final TourOfferRepository tourOfferRepository;
    private final AppUserUtils appUserUtils;

    public HttpStatus addRating(UUID tourOfferId, RatingRequest request) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        TourOffer tourOffer = tourOfferRepository.findById(tourOfferId).
                orElseThrow(() -> new TourOfferNotFoundException(tourOfferId));

        if (tourOffer.getDeletedAt() != null) {
            throw new TourOfferNotFoundException(tourOfferId);
        }

        HttpStatus returnStatus = HttpStatus.CREATED;

        Optional<Rating> optionalRating = ratingRepository.
                findByUserIdAndTourOfferId(user.
                        getId(), tourOfferId);

        Rating rating;

        if (optionalRating.isPresent()) {
            returnStatus = HttpStatus.OK;
            rating = optionalRating.get();
            rating.setRating(request.getRating());
        } else {
            rating = new Rating(request.getRating());
        }

        tourOffer.addRating(rating);
        user.addRating(rating);

        ratingRepository.save(rating);

        return returnStatus;
    }

    public RatingResponse getRating(UUID tourOfferId) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        Optional<Rating> ratingOptional = user.getRatings().stream().filter(
                e -> e.getTourOffer().getId().equals(tourOfferId)).findFirst();

        if (ratingOptional.isEmpty()) {
            return null;
        }

        Rating rating = ratingOptional.get();

        return new RatingResponse(
                tourOfferId,
                rating.getRating(),
                rating.getCreatedAt(),
                rating.getUpdatedAt()
        );
    }
}
