/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

/**
 *
 * @author Adam Bublavý
 */
public class IncorrectUsernameOrPasswordException extends RuntimeException {

    public IncorrectUsernameOrPasswordException(String message) {
        super(message);
    }
}
