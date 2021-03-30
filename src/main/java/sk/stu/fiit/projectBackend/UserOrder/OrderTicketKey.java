/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.UserOrder;

import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

/**
 *
 * @author Adam Bublavý
 */
@Data
public class OrderTicketKey implements Serializable {
    
    private UUID ticketId;
    private UUID orderId;
    
}
