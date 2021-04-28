/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourOffer;

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
import sk.stu.fiit.projectBackend.Rating.Rating;
import sk.stu.fiit.projectBackend.Ticket.Ticket;
import sk.stu.fiit.projectBackend.TourDate.TourDate;
import sk.stu.fiit.projectBackend.TourOffer.dto.CreateTourOfferRequest;
import sk.stu.fiit.projectBackend.User.AppUser;

/**
 * TourOffer holds general data about guide's tour offer.
 *
 * @author Adam Bublavý
 *
 * @see TourDate
 */
@Data
@NoArgsConstructor
@Entity
@Table
public class TourOffer implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid_tour_offer")
    @GenericGenerator(
            name = "uuid_tour_offer",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
            updatable = false,
            nullable = false
    )
    private UUID id;

    @Column(nullable = false)
    private String startPlace;

    @Column(nullable = false)
    private String destinationPlace;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "price_per_person")
    private double pricePerPerson;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @JsonIgnore
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "creatorId",
            referencedColumnName = "id",
            nullable = false,
            updatable = false
    )
    @JsonIgnore
    private AppUser user;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "tourOffer"
    )
    @JsonIgnore
    private List<TourDate> tourDates = new ArrayList<>(0);

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "tourOffer"
    )
    @JsonIgnore
    private List<Rating> ratings = new ArrayList<>(0);

    /**
     * Creates new TourOffer
     *
     * @param startPlace Place where the tour starts
     * @param destinationPlace Place where the tour ends
     * @param description Tour's description
     * @param pricePerPerson Price per ticket
     *
     * @see TourDate
     * @see Ticket
     */
    public TourOffer(String startPlace, String destinationPlace,
            String description, double pricePerPerson) {
        this.startPlace = startPlace;
        this.destinationPlace = destinationPlace;
        this.description = description;
        this.pricePerPerson = pricePerPerson;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Creates new TourOffer from the request
     *
     * @param request TourOffer mapped in CreateTourOfferRequest
     *
     * @see CreateTourOfferRequest
     */
    public TourOffer(CreateTourOfferRequest request) {
        this(request.getStartPlace(), request.getDestinationPlace(), request.
                getDescription(), request.getPricePerPerson());
    }

    /**
     * Adds new TourDate for this TourOffer
     *
     * @param tourDate TourDate which will be added for this TourOffer
     *
     * @see TourDate
     */
    public void addTourDate(TourDate tourDate) {
        if (tourDate == null) {
            return;
        }

        this.tourDates.add(tourDate);
        tourDate.setTourOffer(this);
    }

    /**
     * Removes the tourDate from this TourOffer
     *
     * @param tourDate TourDate which will be removed from this TourOffer
     *
     * @see TourDate
     */
    public void removeTourDate(TourDate tourDate) {
        if (tourDate == null) {
            return;
        }

        this.tourDates.remove(tourDate);
        tourDate.setTourOffer(null);
    }

    /**
     * Adds rating for this TourOffer
     *
     * @param rating User's rating
     *
     * @see Rating
     */
    public void addRating(Rating rating) {
        if (rating == null) {
            return;
        }

        this.ratings.add(rating);
        rating.setTourOffer(this);
    }

    /**
     * Removes rating for this TourOffer
     *
     * @param rating User's rating
     *
     * @see Rating
     */
    public void removeRating(Rating rating) {
        if (rating == null) {
            return;
        }

        this.ratings.remove(rating);
        rating.setTourOffer(null);
    }

}
