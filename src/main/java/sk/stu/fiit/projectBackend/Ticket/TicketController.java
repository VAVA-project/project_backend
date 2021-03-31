/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Ticket;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Adam Bublavý
 */
@RestController
@AllArgsConstructor
@RequestMapping(
        path = "api/v1/tourOffer/{tourOfferId}/tourDate/{tourDateId}/ticket")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/{ticketId}")
    public ResponseEntity<Object> lockTicket(
            @PathVariable("tourOfferId") UUID tourOfferId,
            @PathVariable("tourDateId") UUID tourDateId,
            @PathVariable("ticketId") UUID ticketId) {
        return ResponseEntity.ok(ticketService.lockTicket(tourOfferId,
                tourDateId, ticketId));
    }

}
