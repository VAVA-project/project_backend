/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Order;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.stu.fiit.projectBackend.Order.dto.BookedToursResponse;
import sk.stu.fiit.projectBackend.TourOffer.dto.DataPage;

/**
 *
 * @author Adam Bublavý
 */
@RestController
@RequestMapping("api/v1/users/orders")
@AllArgsConstructor
public class UserOrderController {
    
    private final UserOrderService userOrderService;
    
    @GetMapping(path = "/")
    public ResponseEntity<Page<BookedToursResponse>> getBookedTours(@Valid DataPage page) {
        return ResponseEntity.ok(userOrderService.getBookedTours(page));
    }
    
}
