/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.UserOrder;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import sk.stu.fiit.projectBackend.Ticket.Ticket;

/**
 *
 * @author Adam Bublavý
 */
@Getter
@Setter
@Entity
@Table
@ToString
public class OrderTicket implements Serializable {
    
    @Id
    @GeneratedValue(generator = "uuid_order_ticket")
    @GenericGenerator(
            name = "uuid_order_ticket",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
            updatable = false,
            nullable = false
    )
    private UUID id;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "ticket_id", 
            referencedColumnName = "id",
            nullable = false,
            updatable = false
    )
    private Ticket ticket;
    
}
