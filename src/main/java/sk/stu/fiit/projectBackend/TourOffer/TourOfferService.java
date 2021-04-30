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
import org.springframework.stereotype.Service;
import sk.stu.fiit.projectBackend.Ticket.Ticket;
import sk.stu.fiit.projectBackend.TourDate.TourDate;
import sk.stu.fiit.projectBackend.TourOffer.dto.CreateTourOfferRequest;
import sk.stu.fiit.projectBackend.TourOffer.dto.TourOfferResponse;
import sk.stu.fiit.projectBackend.TourOffer.dto.UpdateTourOfferRequest;
import sk.stu.fiit.projectBackend.User.AppUser;
import sk.stu.fiit.projectBackend.Utils.AppUserUtils;
import sk.stu.fiit.projectBackend.exceptions.TourDateReservedException;
import sk.stu.fiit.projectBackend.exceptions.TourOfferNotFoundException;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class TourOfferService {

    private final TourOfferRepository tourOfferRepository;
    private final AppUserUtils appUserUtils;

    /**
     * Creates new TourOffer
     *
     * @param request Data from the user about TourOffer
     * @return Returns newly created TourOffer mapped into TourOfferResponse
     *
     * @see TourOfferResponse
     * @see AppUser
     * @see TourOffer
     */
    public TourOfferResponse createTourOffer(
            CreateTourOfferRequest request) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        appUserUtils.checkIfIsGuide(user);

        TourOffer newOffer = new TourOffer(request);

        user.addTourOffer(newOffer);

        TourOffer savedOffer = tourOfferRepository.save(newOffer);

        return new TourOfferResponse(savedOffer);
    }

    /**
     * Deletes TourOffer and also all TourDates for this TourOffer
     *
     * @param id ID of TourOffer to delete
     * @return Returns true if deletion was successfull, false otherwise
     *
     * @see TourDate
     * @see TourOffer
     */
    @Transactional
    public boolean deleteTourOffer(UUID id) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        appUserUtils.checkIfIsGuide(user);

        Optional<TourOffer> tourOfferOptional = user.getTourOffers().stream().
                filter(e -> e.getId().equals(id)).findFirst();

        if (!tourOfferOptional.isPresent()) {
            return false;
        }

        TourOffer tourOffer = tourOfferOptional.get();

        if (tourOffer.getDeletedAt() != null) {
            return false;
        }

        for (TourDate tourDate : tourOffer.getTourDates()) {
            for (Ticket ticket : tourDate.getTickets()) {
                if (ticket.getPurchasedAt() != null) {
                    throw new TourDateReservedException();
                }
            }
        }

        tourOffer.setDeletedAt(LocalDateTime.now());
        tourOffer.getTourDates().forEach(e -> {
            e.setDeletedAt(LocalDateTime.now());
        });

        tourOfferRepository.save(tourOffer);

        return true;
    }

    /**
     * Updates TourOffer
     *
     * @param id ID of TourOffer which will be updated
     * @param request Update data from the user
     * @return Returns updated TourOffer mapped into TourOfferResponse
     *
     * @see TourOfferResponse
     * @see AppUser
     * @see TourOffer
     */
    public TourOfferResponse updateTourOffer(UUID id,
            UpdateTourOfferRequest request) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        appUserUtils.checkIfIsGuide(user);

        TourOffer tourOffer = user.getTourOffers().stream().filter(e -> e.
                getId().equals(id)).findFirst().orElseThrow(
                () -> new TourOfferNotFoundException(id));

        if (tourOffer.getDeletedAt() != null) {
            throw new TourOfferNotFoundException(id);
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

        if (updated) {
            tourOffer.setUpdatedAt(LocalDateTime.now());
        }

        TourOffer updatedOffer = tourOfferRepository.save(tourOffer);

        return new TourOfferResponse(updatedOffer);
    }

    /**
     * Gets data about specific TourOffer
     *
     * @param id ID of TourOffer
     * @return Returns data about TourOffer mapped into TourOfferResponse
     *
     * @see TourOffer
     * @see TourOfferResponse
     */
    public TourOfferResponse getTourOffer(UUID id) {
        TourOffer tourOffer = tourOfferRepository.findById(id).orElseThrow(
                () -> new TourOfferNotFoundException(id));

        if (tourOffer.getDeletedAt() != null) {
            throw new TourOfferNotFoundException(id);
        }

        return new TourOfferResponse(tourOffer);
    }

}
