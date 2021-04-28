/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import sk.stu.fiit.projectBackend.Ticket.Ticket;
import sk.stu.fiit.projectBackend.TourOffer.TourOffer;
import sk.stu.fiit.projectBackend.User.AppUser;

/**
 * TourDate holds data about specific tour date. Each TourOffer has several
 * TourDates when it takes place.
 *
 * @author Adam Bublavý
 *
 * @see TourOffer
 */
@Data
@NoArgsConstructor
@Entity
@Table
public class TourDate implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid_tour_date")
    @GenericGenerator(
            name = "uuid_tour_date",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
            updatable = false,
            nullable = false
    )
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private int numberOfTickets;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @JsonIgnore
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "tourOfferId",
            referencedColumnName = "id",
            nullable = false,
            updatable = false
    )
    @JsonIgnore
    private TourOffer tourOffer;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "tourDate"
    )
    @JsonIgnore
    private List<Ticket> tickets = new ArrayList<>(0);

    /**
     * Creates new TourDate
     *
     * @param startDate Date when the tour starts
     * @param endDate Date when the tour ends
     * @param numberOfTickets Number of tickets which can be bought by customers
     * for this TourDate
     * 
     * @see TourOffer
     * @see AppUser
     * @see Ticket
     */
    public TourDate(LocalDateTime startDate, LocalDateTime endDate,
            int numberOfTickets) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfTickets = numberOfTickets;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Adds a new ticket to this TourDate
     * @param ticket Ticket which will be added for this TourDate
     * 
     * @see Ticket
     */
    public void addTicket(Ticket ticket) {
        if (ticket == null) {
            return;
        }

        this.tickets.add(ticket);
        ticket.setTourDate(this);
    }

    /**
     * Removes the ticket from this TourDate
     * @param ticket Ticket which will be removed from this TourDate
     * 
     * @see Ticket
     */
    public void removeTicket(Ticket ticket) {
        if (ticket == null) {
            return;
        }

        this.tickets.remove(ticket);
        ticket.setTourDate(null);
    }

}
