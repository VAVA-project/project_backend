/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourOffer;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.stu.fiit.projectBackend.User.AppUser;

/**
 *
 * @author Adam Bublavý
 */
@Repository
public interface TourOfferRepository extends JpaRepository<TourOffer, UUID> {

    /**
     * Gets all TourOffers which were created by specific user
     *
     * @param id ID of the user
     * @param pageable Data about pagination
     * @return Returns Page of user's TourOffers
     *
     * @see TourOffer
     * @see AppUser
     * @see Page
     */
    @Query("select c from TourOffer c where c.user.id = :creatorId and c.deletedAt is null")
    Page<TourOffer> findAllByUserId(@Param("creatorId") UUID id,
            Pageable pageable);

    /**
     * Finds all TourOffers which startPlace contains expression from
     * startPlaceQuery and destinationPlace which contains expression from
     * destinationPlaceQuery
     *
     * @param startPlaceQuery Search expression for startPlace
     * @param destinationPlaceQuery Search expression for destinationPlace
     * @param pageable Data about pagination
     * @return Returns Page of TourOffers
     * 
     * @see TourOffer
     * @see Page
     */
    Page<TourOffer> findByDeletedAtIsNullAndStartPlaceContainingOrDestinationPlaceContaining(
            String startPlaceQuery, String destinationPlaceQuery,
            Pageable pageable);

}
