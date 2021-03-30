/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourDate;

import java.util.UUID;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sk.stu.fiit.projectBackend.Ticket.Ticket;
import sk.stu.fiit.projectBackend.TourDate.dto.CreateTourDateRequest;
import sk.stu.fiit.projectBackend.TourDate.dto.TourDateResponse;
import sk.stu.fiit.projectBackend.TourDate.dto.UpdateTourDateRequest;
import sk.stu.fiit.projectBackend.TourOffer.TourOffer;
import sk.stu.fiit.projectBackend.User.AppUser;
import sk.stu.fiit.projectBackend.User.AppUserRepository;
import sk.stu.fiit.projectBackend.exceptions.RecordNotFoundException;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class TourDateService {

    private static final String TOUR_OFFER_NOT_FOUND = "Tour offer with id %s not found";
    private static final String TOUR_DATE_NOT_FOUND = "Tour date with id %s not found";

    private final TourDateRepository tourDateRepository;
    private final AppUserRepository appUserRepository;

    @Transactional
    public TourDateResponse createTourDate(UUID id,
            CreateTourDateRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalStateException("invalid date range");
        }

        String userEmail = SecurityContextHolder.getContext().
                getAuthentication().getName();

        AppUser user = appUserRepository.findByEmail(userEmail).get();

        TourOffer tourOffer = user.getTourOffers().stream().filter(e -> e.
                getId().equals(id)).findFirst().orElseThrow(
                () -> new RecordNotFoundException(String.format(
                        TOUR_OFFER_NOT_FOUND, id)));

        if (tourOffer.getDeletedAt() != null) {
            throw new RecordNotFoundException(String.
                    format(TOUR_OFFER_NOT_FOUND, id));
        }

        TourDate newTourDate = new TourDate(request.getStartDate(), request.
                getEndDate());

        tourOffer.getTourDates().add(newTourDate);
        
        for (int index = 0; index < request.getNumberOfTickets(); index++) {
            newTourDate.getTickets().add(new Ticket());
        }
        
        newTourDate = tourDateRepository.save(newTourDate);
        
        return new TourDateResponse(newTourDate, id);
    }

    public TourDateResponse updateTourDate(UUID tourOfferId, UUID tourDateId,
            UpdateTourDateRequest request) {
        if (request.getStartDate() != null && request.getEndDate() != null && request.
                getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalStateException("invalid date range");
        }

        String userEmail = SecurityContextHolder.getContext().
                getAuthentication().getName();

        AppUser user = appUserRepository.findByEmail(userEmail).get();

        TourOffer tourOffer = user.getTourOffers().stream().filter(e -> e.
                getId().equals(tourOfferId)).findFirst().orElseThrow(
                () -> new RecordNotFoundException(String.format(
                        TOUR_OFFER_NOT_FOUND, tourOfferId)));

        if (tourOffer.getDeletedAt() != null) {
            throw new RecordNotFoundException(String.format(
                    TOUR_OFFER_NOT_FOUND, tourOfferId));
        }

        TourDate tourDate = tourOffer.getTourDates().stream().filter(e -> e.
                getId().equals(
                        tourDateId)).findFirst().orElseThrow(
                () -> new RecordNotFoundException(String.format(
                        TOUR_DATE_NOT_FOUND, tourDateId)));

        if (request.getStartDate() != null) {
            tourDate.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            tourDate.setEndDate(request.getEndDate());
        }

        TourDate savedDate = tourDateRepository.save(tourDate);

        return new TourDateResponse(savedDate, tourOfferId);
    }

}
