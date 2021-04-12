/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

import java.util.UUID;
import static sk.stu.fiit.projectBackend.Other.Constants.TICKET_NOT_FOUND;

/**
 *
 * @author Adam Bublavý
 */
public class TicketNotFoundException extends RecordNotFoundException {

    public TicketNotFoundException(UUID id) {
        super(String.format(TICKET_NOT_FOUND, id));
    }
    
}
