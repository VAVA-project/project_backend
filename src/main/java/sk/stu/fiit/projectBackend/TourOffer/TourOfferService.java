/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourOffer;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sk.stu.fiit.projectBackend.TourOffer.dto.CreateTourOfferRequest;
import sk.stu.fiit.projectBackend.TourOffer.dto.TourOfferResponse;
import sk.stu.fiit.projectBackend.TourOffer.dto.UpdateTourOfferRequest;
import sk.stu.fiit.projectBackend.User.AppUser;
import sk.stu.fiit.projectBackend.User.AppUserRepository;
import sk.stu.fiit.projectBackend.exceptions.RecordNotFoundException;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class TourOfferService {

    private static final String USER_NOT_FOUND = "Fatal error, user not found";
    private static final String TOUR_OFFER_NOT_FOUND = "Tour offer with id %s not found";

    private final TourOfferRepository tourOfferRepository;
    private final AppUserRepository appUserRepository;

    @Transactional
    public TourOfferResponse createTourOffer(
            CreateTourOfferRequest request) {

        String userEmail = SecurityContextHolder.getContext().
                getAuthentication().getName();

        AppUser user = appUserRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalStateException(USER_NOT_FOUND)
        );

        TourOffer newOffer = new TourOffer(request.getStartPlace(), request.
                getDestinationPlace(), request.getDescription(), request.
                getPricePerPerson());

        user.getTourOffers().add(newOffer);

        TourOffer savedOffer = tourOfferRepository.save(newOffer);

        return new TourOfferResponse(savedOffer, user.getId());
    }

    public boolean deleteTourOffer(UUID id) {
        String userEmail = SecurityContextHolder.getContext().
                getAuthentication().getName();

        AppUser user = appUserRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalStateException(USER_NOT_FOUND));

        Optional<TourOffer> tourOfferOptional = user.getTourOffers().stream().
                filter(e -> e.getId().equals(id)).findFirst();

        if (!tourOfferOptional.isPresent()) {
            return false;
        }

        TourOffer tourOffer = tourOfferOptional.get();

        if (tourOffer.getDeletedAt() != null) {
            return false;
        }

        tourOffer.setDeletedAt(LocalDateTime.now());

        tourOfferRepository.save(tourOffer);

        return true;
    }

    public TourOfferResponse updateTourOffer(UUID id,
            UpdateTourOfferRequest request) {
        String userEmail = SecurityContextHolder.getContext().
                getAuthentication().getName();

        AppUser user = appUserRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalStateException(USER_NOT_FOUND));

        TourOffer tourOffer = user.getTourOffers().stream().filter(e -> e.
                getId().equals(id)).findFirst().orElseThrow(
                () -> new RecordNotFoundException(String.format(
                        TOUR_OFFER_NOT_FOUND, id)));

        if (tourOffer.getDeletedAt() != null) {
            throw new RecordNotFoundException(String.
                    format(TOUR_OFFER_NOT_FOUND, id));
        }

        if (request.getStartPlace() != null) {
            tourOffer.setStartPlace(request.getStartPlace());
        }
        if (request.getDestinationPlace() != null) {
            tourOffer.setDestinationPlace(request.getDestinationPlace());
        }
        if (request.getDescription() != null) {
            tourOffer.setDescription(request.getDescription());
        }
        if (request.getPricePerPerson() != null) {
            tourOffer.setPricePerPerson(request.getPricePerPerson());
        }

        TourOffer updatedOffer = tourOfferRepository.save(tourOffer);

        return new TourOfferResponse(updatedOffer, user.getId());
    }

}
