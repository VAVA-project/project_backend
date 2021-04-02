/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Ticket;

import java.util.UUID;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.stu.fiit.projectBackend.TourOffer.dto.DataPage;

/**
 *
 * @author Adam Bublavý
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/tickets")
public class TicketController {
    
    private final TicketService ticketService;
    
    @GetMapping(path = "/{tourDateId}/")
    public ResponseEntity<Page<Ticket>> getAvailableTickets(
            @PathVariable("tourDateId") UUID tourDateId,
            @Valid DataPage page
    ) {
        return ResponseEntity.ok(ticketService.getAvailableTickets(tourDateId, page));
    }
    
}
