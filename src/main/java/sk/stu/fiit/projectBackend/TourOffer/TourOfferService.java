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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import static sk.stu.fiit.projectBackend.Other.Constants.TOUR_OFFER_NOT_FOUND;
import sk.stu.fiit.projectBackend.TourOffer.dto.CreateTourOfferRequest;
import sk.stu.fiit.projectBackend.TourOffer.dto.TourOfferPage;
import sk.stu.fiit.projectBackend.TourOffer.dto.TourOfferResponse;
import sk.stu.fiit.projectBackend.TourOffer.dto.UpdateTourOfferRequest;
import sk.stu.fiit.projectBackend.User.AppUser;
import sk.stu.fiit.projectBackend.User.AppUserRepository;
import sk.stu.fiit.projectBackend.Utils.AppUserUtils;
import sk.stu.fiit.projectBackend.exceptions.RecordNotFoundException;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class TourOfferService {

    private final TourOfferRepository tourOfferRepository;
    private final AppUserRepository appUserRepository;
    private final AppUserUtils appUserUtils;

    public TourOfferResponse createTourOffer(
            CreateTourOfferRequest request) {
        
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        TourOffer newOffer = new TourOffer(request);
        
        user.addTourOffer(newOffer);

        TourOffer savedOffer = tourOfferRepository.save(newOffer);

        return new TourOfferResponse(savedOffer);
    }

    @Transactional
    public boolean deleteTourOffer(UUID id) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

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
        tourOffer.getTourDates().forEach(e -> {
            e.setDeletedAt(LocalDateTime.now());
        });

        tourOfferRepository.save(tourOffer);

        return true;
    }

    public TourOfferResponse updateTourOffer(UUID id,
            UpdateTourOfferRequest request) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();
        
        TourOffer tourOffer = user.getTourOffers().stream().filter(e -> e.
                getId().equals(id)).findFirst().orElseThrow(
                () -> new RecordNotFoundException(String.format(
                        TOUR_OFFER_NOT_FOUND, id)));

        if (tourOffer.getDeletedAt() != null) {
            throw new RecordNotFoundException(String.
                    format(TOUR_OFFER_NOT_FOUND, id));
        }
        
        boolean updated = false;

        if (request.getStartPlace() != null) {
            tourOffer.setStartPlace(request.getStartPlace());
            updated = true;
        }
        if (request.getDestinationPlace() != null) {
            tourOffer.setDestinationPlace(request.getDestinationPlace());
            updated = true;
        }
        if (request.getDescription() != null) {
            tourOffer.setDescription(request.getDescription());
            updated = true;
        }
        if (request.getPricePerPerson() != null) {
            tourOffer.setPricePerPerson(request.getPricePerPerson());
            updated = true;
        }
        
        if(updated) tourOffer.setUpdatedAt(LocalDateTime.now());

        TourOffer updatedOffer = tourOfferRepository.save(tourOffer);

        return new TourOfferResponse(updatedOffer);
    }

    public Page<TourOffer> getUsersTourOffers(TourOfferPage page) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();
        
        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.
                getPageSize(), sort);

        return tourOfferRepository.findAllByUserId(user.getId(), pageable);
    }

}
