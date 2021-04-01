/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import sk.stu.fiit.projectBackend.TourDate.Ticket;

/**
 *
 * @author Adam Bublavý
 */
@Data
@NoArgsConstructor
@Entity
@Table
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
            name = "ticketId", 
            referencedColumnName = "id",
            nullable = false,
            updatable = false
    )
    private Ticket ticket;
    
    @Column(
            nullable = false,
            updatable = false
    )
    private double price;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "orderId",
            referencedColumnName = "id",
            nullable = false,
            updatable = false
    )
    @JsonIgnore
    private UserOrder order;

    public OrderTicket(Ticket ticket, double price) {
        this.ticket = ticket;
        this.price = price;
    }

}
