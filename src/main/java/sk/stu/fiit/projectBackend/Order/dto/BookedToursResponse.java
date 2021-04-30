/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Order.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Adam Bublavý
 */
@Data
@AllArgsConstructor
public class BookedToursResponse {
    private List<OrderTicketResponse> orderedTickets;
    
    private LocalDateTime orderTime;
    private String comments;
    
    private double totalPrice;
}
