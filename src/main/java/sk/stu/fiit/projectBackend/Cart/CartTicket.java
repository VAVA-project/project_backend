/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Cart;

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
import sk.stu.fiit.projectBackend.User.AppUser;

/**
 *
 * @author Adam Bublavý
 */
@Data
@NoArgsConstructor
@Entity
@Table
public class CartTicket implements Serializable {
    
    @Id
    @GeneratedValue(generator = "uuid_cart_ticket")
    @GenericGenerator(
            name = "uuid_cart_ticket",
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "userId",
            referencedColumnName = "id",
            nullable = false
    )
    @JsonIgnore
    private AppUser user;
    
    @Column(
            nullable = false,
            updatable = false
    )
    private double price;

    public CartTicket(Ticket ticket, AppUser user, double price) {
        this.ticket = ticket;
        this.user = user;
        this.price = price;
    }
    
}
