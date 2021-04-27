/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author Adam Bublavý
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
        EmailTakenException.class,
        RecordNotFoundException.class,
        IncorrectUsernameOrPasswordException.class,
        InvalidRangeException.class,
        PermissionDeniedException.class,
        TicketIsPurchased.class,
        CartIsEmptyException.class,
        CartIsEmptyException.class,
        TicketNotFoundException.class,
        TourDateNotFoundException.class,
        TourOfferNotFoundException.class,
        UserNotFoundException.class,
        TourDateReservedException.class
    })
    protected ResponseEntity<Object> handleExceptions(RuntimeException e) {
        APIError response = new APIError(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler({TicketPurchaseTimeExpiredException.class})
    protected ResponseEntity<Object> handleTicketsExpiredException(TicketPurchaseTimeExpiredException e) {
        Map<String, List<UUID>> errors = new HashMap<>();
        errors.put("ticket", e.getExpiredTickets());
        
        APIError response = new APIError(HttpStatus.BAD_REQUEST, errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        APIError response = this.formatErrors(ex);

        return ResponseEntity.status(status).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        APIError response = this.formatErrors(ex);

        return ResponseEntity.status(status).body(response);
    }

    private APIError formatErrors(BindException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new APIError(HttpStatus.BAD_REQUEST, errors);
    }

}
