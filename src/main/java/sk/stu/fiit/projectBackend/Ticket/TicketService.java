/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Ticket;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sk.stu.fiit.projectBackend.User.AppUser;
import sk.stu.fiit.projectBackend.User.AppUserRepository;
import sk.stu.fiit.projectBackend.exceptions.RecordNotFoundException;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class TicketService {

    private static final int TICKET_RESERVE_TIME_IN_MINUTES = 15;

    private final TicketRepository ticketRepository;
    private final AppUserRepository appUserRepository;

    @Transactional
    public boolean lockTicket(UUID tourOfferId, UUID tourDateId, UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new RecordNotFoundException("Ticket not found"));

        if (ticket.getPurchasedAt() != null) {
            throw new IllegalStateException("Ticket is already purchased");
        }
        if (ticket.getUser() != null && ticket.getUpdatedAt().plusMinutes(
                TICKET_RESERVE_TIME_IN_MINUTES).isAfter(LocalDateTime.now())) {
            return false;
        }
        
        String userEmail = SecurityContextHolder.getContext().
                getAuthentication().getName();

        AppUser user = appUserRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalStateException("user not found")
        );

        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setUser(user);
        
        ticketRepository.save(ticket);
        appUserRepository.save(user);
        
        return true;
    }

}
