/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourOffer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import sk.stu.fiit.projectBackend.TourDate.TourDate;

/**
 *
 * @author Adam Bublavý
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
@ToString
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
    
    @Column(nullable = false)
    private double pricePerPerson;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    private LocalDateTime deletedAt;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "tourOfferId",
            referencedColumnName = "id",
            nullable = false,
            updatable = false
    )
    private List<TourDate> tourDates;

    public TourOffer(String startPlace, String destinationPlace,
            String description, double pricePerPerson) {
        this.startPlace = startPlace;
        this.destinationPlace = destinationPlace;
        this.description = description;
        this.pricePerPerson = pricePerPerson;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
}
