/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Cart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sk.stu.fiit.projectBackend.Cart.dto.CartResponse;
import sk.stu.fiit.projectBackend.Order.OrderRepository;
import sk.stu.fiit.projectBackend.Order.OrderTicket;
import sk.stu.fiit.projectBackend.Order.UserOrder;
import static sk.stu.fiit.projectBackend.Other.Constants.CART_IS_EMPTY;
import static sk.stu.fiit.projectBackend.Other.Constants.CART_TICKETS_EXPIRED;
import static sk.stu.fiit.projectBackend.Other.Constants.TICKET_ALREADY_PURCHASED;
import static sk.stu.fiit.projectBackend.Other.Constants.TICKET_NOT_FOUND;
import sk.stu.fiit.projectBackend.TourDate.Ticket;
import sk.stu.fiit.projectBackend.TourDate.TicketRepository;
import sk.stu.fiit.projectBackend.User.AppUser;
import sk.stu.fiit.projectBackend.User.AppUserRepository;
import sk.stu.fiit.projectBackend.Utils.AppUserUtils;
import sk.stu.fiit.projectBackend.exceptions.CartIsEmptyException;
import sk.stu.fiit.projectBackend.exceptions.RecordNotFoundException;
import sk.stu.fiit.projectBackend.exceptions.TicketIsPurchased;
import sk.stu.fiit.projectBackend.exceptions.TicketPurchaseTimeExpiredException;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class CartItemService {

    private static final int TICKET_RESERVE_TIME_IN_MINUTES = 15;

    private final TicketRepository ticketRepository;
    private final AppUserRepository appUserRepository;
    private final CartItemRepository cartTicketRepository;
    private final OrderRepository orderRepository;
    private final AppUserUtils appUserUtils;

    @Transactional
    public boolean addTicketToCart(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new RecordNotFoundException(TICKET_NOT_FOUND));

        if (ticket.getPurchasedAt() != null) {
            throw new TicketIsPurchased(TICKET_ALREADY_PURCHASED);
        }

        if (ticket.getLockExpiresAt() != null && LocalDateTime.now()
                .isBefore(ticket.getLockExpiresAt())) {
            return false;
        }

        // WARNING 
        if (ticket.getUser() != null) {
            ticket.getUser().removeTicket(ticket);
        }

        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        ticket.setUpdatedAt(LocalDateTime.now());
        user.addTicket(ticket);
        ticket.setLockExpiresAt(LocalDateTime.now().plusMinutes(
                TICKET_RESERVE_TIME_IN_MINUTES));

        ticketRepository.save(ticket);
        appUserRepository.save(user);

        user.addCartTicket(new CartTicket(ticket, user, ticket.getTourDate().
                getTourOffer().getPricePerPerson()));

        return true;
    }

    public CartResponse getCartContent() {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        List<CartTicket> cartContent = user.getCartTickets();
        double totalPrice = this.calculateTotalPriceForTickets(cartContent);

        return new CartResponse(cartContent, totalPrice);
    }

    @Transactional
    public boolean removeTicketFromCart(UUID ticketId) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new RecordNotFoundException(TICKET_NOT_FOUND));

        CartTicket cartTicket = ticket.getCartTicket();

        user.removeTicket(ticket);
        user.removeCartTicket(cartTicket);
        cartTicketRepository.delete(cartTicket);

        return true;
    }

    @Transactional
    public UserOrder checkout() {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        List<CartTicket> cartTickets = user.getCartTickets();
        if (cartTickets.isEmpty()) {
            throw new CartIsEmptyException(CART_IS_EMPTY);
        }

        // check if all tickets are still locked by me
        cartTickets.stream().
                filter(cartTicket -> (cartTicket.getTicket().getLockExpiresAt().
                isBefore(LocalDateTime.now()) && !cartTicket.getTicket().
                getUser().getId().equals(user.getId()))).
                forEachOrdered(_item -> {
                    throw new TicketPurchaseTimeExpiredException(
                            CART_TICKETS_EXPIRED);
                });

        double totalPrice = calculateTotalPriceForTickets(cartTickets);

        // create new order
        UserOrder order = new UserOrder(LocalDateTime.now(),
                totalPrice, null);
        user.addOrder(order);

        cartTickets.forEach(cartTicket -> {
            cartTicket.getTicket().setPurchasedAt(LocalDateTime.now());
            cartTicket.getTicket().setUpdatedAt(LocalDateTime.now());
            order.addOrderTicket(new OrderTicket(cartTicket.getTicket(),
                    cartTicket.getPrice()));
        });

        cartTickets.clear();

        orderRepository.save(order);
        appUserRepository.save(user);

        return order;
    }

    private double calculateTotalPriceForTickets(List<CartTicket> cartTickets) {
        double totalPrice = 0;

        totalPrice = cartTickets.stream().
                map(ticket -> ticket.getPrice()).
                reduce(totalPrice,
                        (accumulator, _item) -> accumulator + _item);

        return totalPrice;
    }

}
