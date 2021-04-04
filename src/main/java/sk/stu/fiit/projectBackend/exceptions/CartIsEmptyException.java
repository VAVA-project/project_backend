/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

import sk.stu.fiit.projectBackend.Other.Constants;

/**
 *
 * @author Adam Bublavý
 */
public class CartIsEmptyException extends RuntimeException {

    public CartIsEmptyException() {
        super(Constants.CART_IS_EMPTY);
    }

    public CartIsEmptyException(String message) {
        super(message);
    }
    
}
