/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sk.stu.fiit.projectBackend.Order.dto.BookedToursResponse;
import sk.stu.fiit.projectBackend.Order.dto.BookedToursWrapper;
import sk.stu.fiit.projectBackend.Order.dto.OrderTicketResponse;
import sk.stu.fiit.projectBackend.Order.dto.TourDateData;
import sk.stu.fiit.projectBackend.Order.dto.TourOfferData;
import sk.stu.fiit.projectBackend.Rating.RatingService;
import sk.stu.fiit.projectBackend.Rating.dto.RatingResponse;
import sk.stu.fiit.projectBackend.TourDate.TourDate;
import sk.stu.fiit.projectBackend.TourOffer.TourOffer;
import sk.stu.fiit.projectBackend.User.AppUser;
import sk.stu.fiit.projectBackend.Utils.AppUserUtils;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class UserOrderService {

    private final AppUserUtils appUserUtils;
    private final RatingService ratingService;

    /**
     * Scans all user orders and filters out the tickets for tour dates which
     * didn't yet happen
     *
     * @return Returns filtered tickets also with informations about tour offer
     * and tour dates
     *
     * @see BookedToursResponse
     * @see TourDate
     * @see TourOffer
     * @see OrderTicket
     * @see UserOrder
     */
    public BookedToursWrapper getBookedTours() {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        Map<UUID, TourDate> tourDates = new HashMap<>();
        Map<UUID, TourOffer> tourOffers = new HashMap<>();

        List<BookedToursResponse> result = new ArrayList<>();

        List<UserOrder> userOrders = user.getOrders();
        userOrders.stream().forEach(e -> {
            List<OrderTicket> orderedTickets = e.getOrderTickets();

            List<OrderTicket> bookedTickets = new ArrayList<>();

            orderedTickets.stream().
                    filter(ticket -> ticket.getTicket().getTourDate().
                    getEndDate().isAfter(LocalDateTime.now())).
                    map(ticket -> {
                        TourDate tourDate = ticket.getTicket().getTourDate();
                        bookedTickets.add(ticket);
                        tourDates.putIfAbsent(tourDate.getId(), tourDate);
                        tourOffers.putIfAbsent(tourDate.getTourOffer().getId(),
                                tourDate.getTourOffer());
                        return tourDate;
                    }).collect(Collectors.toList());

            List<OrderTicketResponse> bookedTicketsResponse = this.
                    mapOrderTicketsToOrderTicketResponse(bookedTickets);

            if (!bookedTickets.isEmpty()) {
                result.add(new BookedToursResponse(
                        bookedTicketsResponse,
                        e.getOrderTime(),
                        e.getComments(),
                        e.getTotalPrice()
                ));
            }
        });

        List<TourDateData> tourDatesData = this.mapTourDatesToTourDateData(
                tourDates.values().stream().collect(Collectors.toList()));

        List<TourOfferData> tourOfferData = this.mapTourOffersToTourOfferData(
                tourOffers.values().stream().collect(Collectors.toList()));

        return new BookedToursWrapper(result, tourDatesData, tourOfferData);
    }

    /**
     * Scans all user orders and filters out the tickets for tour dates which
     * have happened
     *
     * @return Returns filtered tickets also with informations about tour offer
     * and tour dates
     *
     * @see BookedToursResponse
     * @see TourDate
     * @see TourOffer
     * @see OrderTicket
     * @see UserOrder
     */
    public BookedToursWrapper getCompletedTours() {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        Map<UUID, TourDate> tourDates = new HashMap<>();
        Map<UUID, TourOffer> tourOffers = new HashMap<>();

        List<BookedToursResponse> result = new ArrayList<>();

        List<UserOrder> userOrders = user.getOrders();
        userOrders.stream().forEach(e -> {
            List<OrderTicket> orderedTickets = e.getOrderTickets();

            List<OrderTicket> bookedTickets = new ArrayList<>();

            orderedTickets.stream().
                    filter(ticket -> ticket.getTicket().getTourDate().
                    getEndDate().isBefore(LocalDateTime.now())).
                    map(ticket -> {
                        TourDate tourDate = ticket.getTicket().getTourDate();
                        bookedTickets.add(ticket);
                        tourDates.putIfAbsent(tourDate.getId(), tourDate);
                        tourOffers.putIfAbsent(tourDate.getTourOffer().getId(),
                                tourDate.getTourOffer());
                        return tourDate;
                    }).collect(Collectors.toList());

            List<OrderTicketResponse> bookedTicketsResponse = this.
                    mapOrderTicketsToOrderTicketResponse(bookedTickets);

            if (!bookedTickets.isEmpty()) {
                result.add(new BookedToursResponse(
                        bookedTicketsResponse,
                        e.getOrderTime(),
                        e.getComments(),
                        e.getTotalPrice()
                ));
            }
        });

        List<TourDateData> tourDatesData = this.mapTourDatesToTourDateData(
                tourDates.values().stream().collect(Collectors.toList()));

        List<TourOfferData> tourOfferData = this.mapTourOffersToTourOfferData(
                tourOffers.values().stream().collect(Collectors.toList()));

        return new BookedToursWrapper(result, tourDatesData, tourOfferData);
    }

    /**
     * Maps OrderTicket to OrderTicketResponse
     *
     * @param orderTickets list of OrderTickets which will be mapped
     * @return Returns list of OrderTicketResponses
     *
     * @see OrderTicket
     * @see OrderTicketResponse
     */
    private List<OrderTicketResponse> mapOrderTicketsToOrderTicketResponse(
            List<OrderTicket> orderTickets) {
        return orderTickets.stream().map(
                ticket -> {
                    TourDate tourDate = ticket.getTicket().
                            getTourDate();

                    return new OrderTicketResponse(tourDate.
                            getTourOffer().
                            getId(), tourDate.getId(),
                            ticket.getTicket().getId(),
                            ticket.getPrice(),
                            ticket.getTicket().getPurchasedAt());
                }).collect(Collectors.toList());
    }

    /**
     * Maps TourDates to TourDateData which will be sent in response
     *
     * @param tourDates List of tour dates which will be mapped
     * @return Returns list of mapped TourDateData
     *
     * @see TourDate
     * @see TourDateData
     */
    private List<TourDateData> mapTourDatesToTourDateData(
            List<TourDate> tourDates) {
        return tourDates.stream().map(tourDate -> {
            return new TourDateData(
                    tourDate.getId(),
                    tourDate.getStartDate(),
                    tourDate.getEndDate());
        }).collect(Collectors.toList());
    }

    /**
     * Maps TourOffers to TourOfferData which will be sent in response
     *
     * @param tourOffers List of tour offers which will be mapped
     * @return Returns list of mapped TourOfferData
     *
     * @see TourOffer
     * @see TourOfferData
     */
    private List<TourOfferData> mapTourOffersToTourOfferData(
            List<TourOffer> tourOffers) {
        return tourOffers.stream().map(tourOffer -> {
            RatingResponse ratingResponse = ratingService.getRating(
                    tourOffer.getId());

            return new TourOfferData(
                    tourOffer.getId(),
                    tourOffer.getStartPlace(),
                    tourOffer.getDescription(),
                    tourOffer.getDescription(),
                    ratingResponse == null ? -1 : ratingResponse.
                                    getRating()
            );
        }).collect(Collectors.toList());
    }

}
