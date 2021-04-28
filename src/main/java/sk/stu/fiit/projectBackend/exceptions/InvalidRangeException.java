/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

/**
 * InvalidRangeException is thrown when the user has provided value for variable
 * which is out of range
 *
 * @author Adam Bublavý
 */
public class InvalidRangeException extends RuntimeException {

    public InvalidRangeException(String message) {
        super(message);
    }

}
