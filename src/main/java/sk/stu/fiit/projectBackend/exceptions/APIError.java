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
 *
 * @author Adam Bublavý
 */
@Data
public class APIError {
    
    private HttpStatus httpStatus;
    private LocalDateTime timestamp;
    private Object errors;
    
    private APIError() {
        this.timestamp = LocalDateTime.now();
    }

    public APIError(HttpStatus httpStatus, Object errors) {
        this();
        this.httpStatus = httpStatus;
        this.errors = errors;
    }
    
}
