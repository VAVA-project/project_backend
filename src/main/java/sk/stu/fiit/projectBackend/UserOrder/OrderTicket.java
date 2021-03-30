/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.UserOrder;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Adam Bublavý
 */
@Getter
@Setter
@Entity
@Table
@ToString
@IdClass(OrderTicketKey.class)
public class OrderTicket implements Serializable {
    
    @Id
    private UUID ticketId;
    
    @Id
    private UUID orderId;
    
}
