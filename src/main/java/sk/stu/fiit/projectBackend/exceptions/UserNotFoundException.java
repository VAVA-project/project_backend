/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

import java.util.UUID;
import static sk.stu.fiit.projectBackend.Other.Constants.USER_WITH_ID_NOT_FOUND;

/**
 * UserNotFoundException is thrown when user with specific ID is not found
 *
 * @author Adam Bublavý
 */
public class UserNotFoundException extends RecordNotFoundException {

    public UserNotFoundException(UUID id) {
        super(String.format(USER_WITH_ID_NOT_FOUND, id));
    }

}
