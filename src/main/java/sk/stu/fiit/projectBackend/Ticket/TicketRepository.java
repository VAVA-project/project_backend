/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Ticket;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.stu.fiit.projectBackend.TourDate.TourDate;

/**
 *
 * @author Adam Bublavý
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    /**
     * Finds available tickets for specific TourDate. It means the tickets which
     * are not locked by other users or which are not already purchased.
     *
     * @param dateId ID of TourDate
     * @param pageable Data about paging
     * @return Returns Page of available tickets
     *
     * @see TourDate
     * @see Ticket
     */
    @Query("select c from Ticket c where c.tourDate.id = ?1 and c.tourDate.deletedAt is null and c.purchasedAt is null and (c.lockExpiresAt is null or c.lockExpiresAt < now())")
    Page<Ticket> findAvailableTickets(UUID dateId, Pageable pageable);

    /**
     * Counts sold tickets for specific TourDate
     *
     * @param tourDateId ID of TourDate
     * @return Returns number of sold tickets
     *
     * @see TourDate
     * @see Ticket
     */
    @Query("select count(*) from Ticket c where c.purchasedAt is not null and c.tourDate.id = ?1")
    Optional<Integer> countSoldTickets(UUID tourDateId);

}
