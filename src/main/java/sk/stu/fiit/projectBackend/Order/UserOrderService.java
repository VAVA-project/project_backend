/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Order;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sk.stu.fiit.projectBackend.Order.dto.BookedToursResponse;
import sk.stu.fiit.projectBackend.TourOffer.dto.DataPage;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class UserOrderService {
    
    private final UserOrderRepository userOrderRepository;
    
    public Page<BookedToursResponse> getBookedTours(DataPage page) {
        
        return null;
    }
    
}
