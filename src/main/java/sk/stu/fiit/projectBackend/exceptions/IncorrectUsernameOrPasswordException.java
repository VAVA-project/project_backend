/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

import static sk.stu.fiit.projectBackend.Other.Constants.INCORRECT_USERNAME_OR_PASSWORD;

/**
 *
 * @author Adam Bublavý
 */
public class IncorrectUsernameOrPasswordException extends RuntimeException {

    public IncorrectUsernameOrPasswordException() {
        super(INCORRECT_USERNAME_OR_PASSWORD);
    }

    public IncorrectUsernameOrPasswordException(String message) {
        super(message);
    }
}
