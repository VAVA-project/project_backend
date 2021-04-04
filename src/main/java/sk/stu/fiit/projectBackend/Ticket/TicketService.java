/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Ticket;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sk.stu.fiit.projectBackend.TourOffer.dto.DataPage;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class TicketService {
    
    private final TicketRepository ticketRepository;
    
    public Page<Ticket> getAvailableTickets(UUID dateId, DataPage page) {
        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.
                getPageSize(), sort);

        return ticketRepository.findAvailableTickets(dateId, pageable);
    }
    
}
