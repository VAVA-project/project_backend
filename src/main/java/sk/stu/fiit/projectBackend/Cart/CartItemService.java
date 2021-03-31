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
import static sk.stu.fiit.projectBackend.Other.Constants.TICKET_ALREADY_PURCHASED;
import static sk.stu.fiit.projectBackend.Other.Constants.TICKET_NOT_FOUND;
import sk.stu.fiit.projectBackend.TourDate.Ticket;
import sk.stu.fiit.projectBackend.TourDate.TicketRepository;
import sk.stu.fiit.projectBackend.User.AppUser;
import sk.stu.fiit.projectBackend.User.AppUserRepository;
import sk.stu.fiit.projectBackend.Utils.AppUserUtils;
import sk.stu.fiit.projectBackend.exceptions.RecordNotFoundException;
import sk.stu.fiit.projectBackend.exceptions.TicketIsPurchased;

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
    private final AppUserUtils appUserUtils;
    
    @Transactional
    public boolean addTicketToCart(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new RecordNotFoundException(TICKET_NOT_FOUND));
        
        if (ticket.getPurchasedAt() != null) {
            throw new TicketIsPurchased(TICKET_ALREADY_PURCHASED);
        }
        
        if (ticket.getUser() != null && LocalDateTime.now().isBefore(ticket.
                getLockExpiresAt())) {
            return false;
        }
        
        AppUser user = appUserUtils.getCurrentlyLoggedUser();
        
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setUser(user);
        ticket.setLockExpiresAt(LocalDateTime.now().plusMinutes(
                TICKET_RESERVE_TIME_IN_MINUTES));
        
        ticketRepository.save(ticket);
        appUserRepository.save(user);
        
        user.addCartTicket(new CartTicket(ticket, user, ticket.getTourDate().getTourOffer().getPricePerPerson()));
        
        return true;
    }
    
    public CartResponse getCartContent() {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();
        
        List<CartTicket> cartContent = user.getCartTickets();
        double totalPrice = 0;
        
        totalPrice = cartContent.stream().
                map(ticket -> ticket.getPrice()).
                reduce(totalPrice,
                        (accumulator, _item) -> accumulator + _item);
        
        return new CartResponse(cartContent, totalPrice);
    }
    
}
