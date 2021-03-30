/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.stu.fiit.projectBackend.TourOffer.TourOffer;

/**
 *
 * @author Adam Bublavý
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID>{
    Optional<AppUser> findByEmail(String email);
    
    @Query("select c.tourOffers from AppUser c where c.id = :creatorId")
    Page<TourOffer> findAllByUserId(@Param("creatorId") UUID id, Pageable pageable);
}
