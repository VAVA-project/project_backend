/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

import java.util.UUID;
import static sk.stu.fiit.projectBackend.Other.Constants.CART_TICKETS_EXPIRED;

/**
 *
 * @author Adam Bublavý
 */
public class TicketPurchaseTimeExpiredException extends RuntimeException {

    public TicketPurchaseTimeExpiredException(String message) {
        super(message);
    }

    public TicketPurchaseTimeExpiredException(UUID id) {
        super(String.format(CART_TICKETS_EXPIRED, id));
    }
    
}
