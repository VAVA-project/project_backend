/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

import static sk.stu.fiit.projectBackend.Other.Constants.EMAIL_ALREADY_TAKEN;

/**
 *
 * @author Adam Bublavý
 */
public class EmailTakenException extends RuntimeException {

    public EmailTakenException() {
        super(EMAIL_ALREADY_TAKEN);
    }

    public EmailTakenException(String msg) {
        super(msg);
    }
    
}
