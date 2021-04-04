/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Ticket;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Adam Bublavý
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    
    @Query("select c from Ticket c where c.tourDate.id = ?1 and c.tourDate.deletedAt is null and (c.lockExpiresAt is null or c.lockExpiresAt < now())")
    Page<Ticket> findAvailableTickets(UUID dateId, Pageable pageable);
    
}
