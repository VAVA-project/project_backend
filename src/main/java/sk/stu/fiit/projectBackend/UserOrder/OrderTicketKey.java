/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.UserOrder;

import java.io.Serializable;
import java.util.UUID;
import lombok.Data;
import sk.stu.fiit.projectBackend.Ticket.Ticket;

/**
 *
 * @author Adam Bublavý
 */
@Data
public class OrderTicketKey implements Serializable {
    
    private Ticket ticket;
    private UUID orderId;
    
}
