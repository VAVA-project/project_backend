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

/**
 *
 * @author Adam Bublavý
 */
@Repository
public interface TourOfferRepository extends JpaRepository<TourOffer, UUID> {
    
    @Query("select c from TourOffer c where c.user.id = :creatorId and c.deletedAt is null")
    Page<TourOffer> findAllByUserId(@Param("creatorId") UUID id, Pageable pageable);
    
    Page<TourOffer> findByStartPlaceContainingOrDestinationPlaceContaining(String startPlaceQuery, String destinationPlaceQuery, Pageable pageable);
    
}
