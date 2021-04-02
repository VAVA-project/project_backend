/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Cart;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.stu.fiit.projectBackend.Cart.dto.CartResponse;
import sk.stu.fiit.projectBackend.Order.UserOrder;

/**
 *
 * @author Adam Bublavý
 */
@RestController
@RequestMapping(path = "api/v1/cart")
@AllArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping(path = "/ticket/{ticketId}/")
    public ResponseEntity<Boolean> addTicketToCart(
            @PathVariable("ticketId") UUID id) {
        return ResponseEntity.ok(cartItemService.addTicketToCart(id));
    }

    @DeleteMapping(path = "/ticket/{ticketId}/")
    public ResponseEntity<Boolean> removeTicketFromCart(
            @PathVariable("ticketId") UUID id) {
        return ResponseEntity.ok(cartItemService.removeTicketFromCart(id));
    }

    @GetMapping(path = "/")
    public ResponseEntity<CartResponse> getCartContent() {
        return ResponseEntity.ok(cartItemService.getCartContent());
    }
    
    @PostMapping(path = "/checkout/")
    public ResponseEntity<UserOrder> checkout() {
        return ResponseEntity.ok(cartItemService.checkout());
    }
    
}
