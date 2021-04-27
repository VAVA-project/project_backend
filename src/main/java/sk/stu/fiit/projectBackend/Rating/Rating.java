/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Rating;

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
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import sk.stu.fiit.projectBackend.TourOffer.TourOffer;
import sk.stu.fiit.projectBackend.User.AppUser;

/**
 * Rating represents user's rating for specific TourOffer
 *
 * @author Adam Bublavý
 *
 * @see AppUser
 * @see TourOffer
 */
@Data
@NoArgsConstructor
@Entity
@Table
public class Rating implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid_rating")
    @GenericGenerator(
            name = "uuid_rating",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
            updatable = false,
            nullable = false
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "creatorId",
            referencedColumnName = "id",
            nullable = false,
            updatable = false
    )
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "tourOfferId",
            referencedColumnName = "id",
            nullable = false,
            updatable = false
    )
    private TourOffer tourOffer;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Rating(int rating) {
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
