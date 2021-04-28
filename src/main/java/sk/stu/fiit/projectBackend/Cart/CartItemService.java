/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Cart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sk.stu.fiit.projectBackend.Cart.dto.CartResponse;
import sk.stu.fiit.projectBackend.Cart.dto.CheckoutRequest;
import sk.stu.fiit.projectBackend.Order.OrderTicket;
import sk.stu.fiit.projectBackend.Order.UserOrder;
import sk.stu.fiit.projectBackend.Order.UserOrderRepository;
import static sk.stu.fiit.projectBackend.Other.Constants.TICKET_ALREADY_PURCHASED;
import sk.stu.fiit.projectBackend.Ticket.Ticket;
import sk.stu.fiit.projectBackend.Ticket.TicketRepository;
import sk.stu.fiit.projectBackend.User.AppUser;
import sk.stu.fiit.projectBackend.User.AppUserRepository;
import sk.stu.fiit.projectBackend.Utils.AppUserUtils;
import sk.stu.fiit.projectBackend.exceptions.CartIsEmptyException;
import sk.stu.fiit.projectBackend.exceptions.TicketIsPurchased;
import sk.stu.fiit.projectBackend.exceptions.TicketNotFoundException;
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
    private final UserOrderRepository orderRepository;
    private final AppUserUtils appUserUtils;

    /**
     * Adds the ticket to the user's cart.
     *
     * @param ticketId ID of a ticket which will be added to the user's cart
     * @return True, if the ticket was successfully added to the user's cart,
     * false otherwise
     */
    @Transactional
    public boolean addTicketToCart(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new TicketNotFoundException(ticketId));

        if (ticket.getPurchasedAt() != null) {
            throw new TicketIsPurchased(TICKET_ALREADY_PURCHASED);
        }

        if (ticket.getLockExpiresAt() != null && LocalDateTime.now()
                .isBefore(ticket.getLockExpiresAt())) {
            return false;
        }

        // WARNING 
        if (ticket.getUser() != null) {
            CartTicket cartTicket = ticket.getCartTicket();

            ticket.getUser().removeCartTicket(cartTicket);
            cartTicketRepository.delete(cartTicket);

            ticket.getUser().removeTicket(ticket);
        }

        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        user.addTicket(ticket);
        ticket.setLockExpiresAt(LocalDateTime.now().plusMinutes(
                TICKET_RESERVE_TIME_IN_MINUTES));

        ticketRepository.save(ticket);
        appUserRepository.save(user);

        user.addCartTicket(new CartTicket(ticket, user, ticket.getTourDate().
                getTourOffer().getPricePerPerson()));

        return true;
    }

    /**
     * @return Returns current content of the user's cart
     *
     * @see CartResponse
     */
    public CartResponse getCartContent() {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        List<CartTicket> cartContent = user.getCartTickets();
        double totalPrice = this.calculateTotalPriceForTickets(cartContent);

        return new CartResponse(cartContent, totalPrice);
    }

    /**
     * Removes the ticket from the user's cart.
     *
     * @param ticketId ID of the ticket which will be removed from the user's
     * cart
     * @return Returns true, if the ticket was successfully removed from the
     * cart, false otherwise
     */
    @Transactional
    public boolean removeTicketFromCart(UUID ticketId) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new TicketNotFoundException(ticketId));

        CartTicket cartTicket = ticket.getCartTicket();

        if (cartTicket == null) {
            return false;
        }

        user.removeTicket(ticket);
        user.removeCartTicket(cartTicket);
        cartTicketRepository.delete(cartTicket);

        return true;
    }

    /**
     * Checkouts cart.
     *
     * @param request Request which contains additional informations about
     * checkout
     * @return Returns UserOrder which contains ordered tickets
     *
     * @see CheckoutRequest
     * @see UserOrder
     */
    @Transactional
    public UserOrder checkout(CheckoutRequest request) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        List<CartTicket> cartTickets = user.getCartTickets();
        if (cartTickets.isEmpty()) {
            throw new CartIsEmptyException();
        }

        // check if all tickets are still locked by me and if tour date is not deleted
        List<CartTicket> expiredTickets = cartTickets.stream().
                filter(cartTicket -> (cartTicket.getTicket().getLockExpiresAt().
                isBefore(LocalDateTime.now())
                || !cartTicket.getTicket().getUser().getId().
                        equals(user.getId())
                || cartTicket.getTicket().getTourDate().getDeletedAt() != null)).
                collect(Collectors.toList());

        if (!expiredTickets.isEmpty()) {
            throw new TicketPurchaseTimeExpiredException(
                    expiredTickets.stream().map(e -> e.getTicket().getId()).
                            collect(
                                    Collectors.toList()));
        }

        double totalPrice = calculateTotalPriceForTickets(cartTickets);

        // create a new order
        UserOrder order = new UserOrder(LocalDateTime.now(),
                totalPrice, request.getComments());
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

    /**
     * Clears content of the user's cart
     *
     * @return Returns HttpStatus.NO_CONTENT which tells that the cart was
     * cleared
     */
    @Transactional
    public HttpStatus clearCart() {
        AppUser user = this.appUserUtils.getCurrentlyLoggedUser();

        List<CartTicket> cartTickets = user.getCartTickets();

        cartTickets.forEach(e -> {
            user.removeTicket(e.getTicket());
        });

        cartTickets.clear();

        return HttpStatus.NO_CONTENT;
    }

    /**
     * Calculates total price of tickets.
     *
     * @param cartTickets List of tickets of which the total price will be
     * calculated
     * @return Returns total price of tickets
     */
    private double calculateTotalPriceForTickets(List<CartTicket> cartTickets) {
        double totalPrice = 0;

        totalPrice = cartTickets.stream().
                map(ticket -> ticket.getPrice()).
                reduce(totalPrice,
                        (accumulator, _item) -> accumulator + _item);

        return totalPrice;
    }

}
