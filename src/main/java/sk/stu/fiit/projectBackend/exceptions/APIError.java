/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * APIError represents generic error which is returned to the user
 *
 * @author Adam Bublavý
 */
@Data
public class APIError {

    private HttpStatus httpStatus;
    private LocalDateTime timestamp;
    private Object errors;

    /**
     * Creates new empty APIError
     */
    private APIError() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Creates new APIError
     *
     * @param httpStatus HttpStatus of the error
     * @param errors Errors
     */
    public APIError(HttpStatus httpStatus, Object errors) {
        this();
        this.httpStatus = httpStatus;
        this.errors = errors;
    }

}
