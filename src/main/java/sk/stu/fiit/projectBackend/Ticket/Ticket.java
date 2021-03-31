/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Ticket;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import sk.stu.fiit.projectBackend.Cart.CartTicket;
import sk.stu.fiit.projectBackend.TourDate.TourDate;
import sk.stu.fiit.projectBackend.User.AppUser;

/**
 *
 * @author Adam Bublavý
 */
@Getter
@Setter
@Entity
@Table
@ToString
public class Ticket implements Serializable {
    
    @Id
    @GeneratedValue(generator = "uuid_ticket")
    @GenericGenerator(
            name = "uuid_ticket",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
            updatable = false,
            nullable = false
    )
    private UUID id;
    
    private LocalDateTime purchasedAt;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    private LocalDateTime deletedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private AppUser user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tourDateId")
    private TourDate tourDate;
    
    @OneToOne(mappedBy = "ticket")
    private CartTicket cartTicket;

    public Ticket() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
