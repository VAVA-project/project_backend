/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

import static sk.stu.fiit.projectBackend.Other.Constants.EMAIL_ALREADY_TAKEN;

/**
 * EmailTakenException is thrown when user wants to register with the email that
 * somebody has used before
 *
 * @author Adam Bublavý
 */
public class EmailTakenException extends RuntimeException {

    public EmailTakenException(String email) {
        super(String.format(EMAIL_ALREADY_TAKEN, email));
    }
}
