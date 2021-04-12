/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Adam Bublavý
 */
@Data
@AllArgsConstructor
public class BookedToursResponse {
    @Data
    @AllArgsConstructor
    private class TicketDataResponse {
        private UUID ticketId;
        private LocalDateTime purchasedAt;
        
        private UUID tourDateId;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
                
        private UUID tourOfferId;
        private String startPlace;
        private String destinationPlace;
        private String description;
        private double pricePerPerson;
    }
    
    private List<TicketDataResponse> tickets;
    private LocalDateTime orderTime;
}
