/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Order.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Adam Bublavý
 */
@Data
@AllArgsConstructor
public class OrderTicketResponse {
    private UUID tourOfferId;
    private UUID tourDateId;
    private UUID ticketId;
    
    private double price;
    
    private LocalDateTime purchasedAt;
    
}
