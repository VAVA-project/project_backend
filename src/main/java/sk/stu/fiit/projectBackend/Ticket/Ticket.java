/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Ticket;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import sk.stu.fiit.projectBackend.Cart.CartTicket;
import sk.stu.fiit.projectBackend.TourDate.TourDate;
import sk.stu.fiit.projectBackend.User.AppUser;

/**
 * Ticket holds data about ticket for specific TourDate.
 *
 * @author Adam Bublavý
 *
 * @see TourDate
 * @see TourOffer
 * @see CartTicket
 * @see AppUser
 */
@Data
@Entity
@Table
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

    /**
     * When this ticket is added to the user's cart, the ticket is locked for
     * specific amount of time to prevent other users to lock this ticket. When
     * the time passes, the ticket will be unlocked again for other users.
     */
    private LocalDateTime lockExpiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @JsonIgnore
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "tourDateId",
            nullable = false,
            updatable = false
    )
    @JsonIgnore
    private TourDate tourDate;

    @OneToOne(mappedBy = "ticket")
    @JsonIgnore
    private CartTicket cartTicket;

    public Ticket() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
