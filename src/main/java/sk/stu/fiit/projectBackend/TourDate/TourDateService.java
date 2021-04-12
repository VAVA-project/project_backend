/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourDate;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import static sk.stu.fiit.projectBackend.Other.Constants.TOUR_DATE_INVALID_DATES;
import sk.stu.fiit.projectBackend.Ticket.Ticket;
import sk.stu.fiit.projectBackend.TourDate.dto.CreateTourDateRequest;
import sk.stu.fiit.projectBackend.TourDate.dto.TourDateResponse;
import sk.stu.fiit.projectBackend.TourDate.dto.UpdateTourDateRequest;
import sk.stu.fiit.projectBackend.TourOffer.TourOffer;
import sk.stu.fiit.projectBackend.TourOffer.dto.DataPage;
import sk.stu.fiit.projectBackend.User.AppUser;
import sk.stu.fiit.projectBackend.Utils.AppUserUtils;
import sk.stu.fiit.projectBackend.exceptions.InvalidRangeException;
import sk.stu.fiit.projectBackend.exceptions.TourDateNotFoundException;
import sk.stu.fiit.projectBackend.exceptions.TourOfferNotFoundException;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class TourDateService {

    private final TourDateRepository tourDateRepository;
    private final AppUserUtils appUserUtils;

    @Transactional
    public TourDateResponse createTourDate(UUID id,
            CreateTourDateRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new InvalidRangeException(TOUR_DATE_INVALID_DATES);
        }

        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        TourOffer tourOffer = user.getTourOffers().stream().filter(e -> e.
                getId().equals(id)).findFirst().orElseThrow(
                () -> new TourOfferNotFoundException(id));

        if (tourOffer.getDeletedAt() != null) {
            throw new TourOfferNotFoundException(id);
        }

        TourDate newTourDate = new TourDate(request.getStartDate(), request.
                getEndDate());

        tourOffer.addTourDate(newTourDate);

        for (int index = 0; index < request.getNumberOfTickets(); index++) {
            newTourDate.addTicket(new Ticket());
        }

        newTourDate = tourDateRepository.save(newTourDate);

        return new TourDateResponse(newTourDate, id);
    }

    public TourDateResponse updateTourDate(UUID tourOfferId, UUID tourDateId,
            UpdateTourDateRequest request) {
        if (request.getStartDate() != null && request.getEndDate() != null && request.
                getStartDate().isAfter(request.getEndDate())) {
            throw new InvalidRangeException(TOUR_DATE_INVALID_DATES);
        }

        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        TourOffer tourOffer = user.getTourOffers().stream().filter(e -> e.
                getId().equals(tourOfferId)).findFirst().orElseThrow(
                () -> new TourOfferNotFoundException(tourOfferId));

        if (tourOffer.getDeletedAt() != null) {
            throw new TourOfferNotFoundException(tourOfferId);
        }

        TourDate tourDate = tourOffer.getTourDates().stream().filter(e -> e.
                getId().equals(
                        tourDateId)).findFirst().orElseThrow(
                () -> new TourDateNotFoundException(tourDateId));

        if (tourDate.getDeletedAt() != null) {
            throw new TourDateNotFoundException(tourDateId);
        }

        LocalDateTime testStartDate = request.getStartDate() != null ? request.
                getStartDate() : tourDate.getStartDate();
        LocalDateTime testEndDate = request.getEndDate() != null ? request.
                getEndDate() : tourDate.getEndDate();

        if (testStartDate.isAfter(testEndDate)) {
            throw new InvalidRangeException(TOUR_DATE_INVALID_DATES);
        }

        tourDate.setStartDate(testStartDate);
        tourDate.setEndDate(testEndDate);
        tourDate.setUpdatedAt(LocalDateTime.now());

        tourDate = tourDateRepository.save(tourDate);

        return new TourDateResponse(tourDate, tourOfferId);
    }

    public HttpStatus deleteTourDate(UUID tourId, UUID dateId) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        TourOffer tourOffer = user.getTourOffers().stream().filter(e -> e.
                getId().equals(tourId)).findFirst().orElseThrow(
                () -> new TourOfferNotFoundException(tourId));

        if (tourOffer.getDeletedAt() != null) {
            throw new TourOfferNotFoundException(tourId);
        }

        TourDate tourDate = tourOffer.getTourDates().stream().filter(e -> e.
                getId().equals(
                        dateId)).findFirst().orElseThrow(
                () -> new TourDateNotFoundException(dateId));

        if (tourDate.getDeletedAt() != null) {
            throw new TourDateNotFoundException(dateId);
        }

        tourDate.setDeletedAt(LocalDateTime.now());
        tourDateRepository.save(tourDate);

        return HttpStatus.NO_CONTENT;
    }

    public Page<TourDate> getTourDates(UUID dateId, DataPage page) {
        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.
                getPageSize(), sort);

        return tourDateRepository.
                findByTourOfferIdAndDeletedAtIsNullAndStartDateGreaterThanEqual(
                        dateId,
                        LocalDateTime.now(),
                        pageable);
    }

}
