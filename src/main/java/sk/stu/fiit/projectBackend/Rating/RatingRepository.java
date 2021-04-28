/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Rating;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Adam Bublavý
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {

    Optional<Rating> findByUserIdAndTourOfferId(UUID userId, UUID tourId);

    @Query("SELECT AVG(e.rating) FROM Rating e WHERE e.tourOffer.id = :tourOfferId")
    Optional<Double> calculateAverageRating(
            @Param("tourOfferId") UUID tourOfferId);

}
