/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Order;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.stu.fiit.projectBackend.User.AppUser;

/**
 *
 * @author Adam Bublavý
 */
@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder, UUID> {
    
    /**
     * @param userId ID of user
     * @return Returns list of all UserOrder for given user
     * 
     * @see UserOrder
     * @see AppUser
     */
    List<UserOrder> findAllByUserId(UUID userId);
    
}
