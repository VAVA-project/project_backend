/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

import static sk.stu.fiit.projectBackend.Other.Constants.PERMISSION_DENIED;

/**
 * PermissionDeniedException is thrown when the user has no permission for
 * specific action
 *
 * @author Adam Bublavý
 */
public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException() {
        super(PERMISSION_DENIED);
    }

    public PermissionDeniedException(String message) {
        super(message);
    }

}
