/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Rating.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Adam Bublavý
 */
@AllArgsConstructor
@Getter
@Setter
public class RatingRequest {
    
    @NotNull(message = "required")
    @Min(value = 0, message = "value must be between 0 and 5")
    @Max(value = 5, message = "value must be between 0 and 5")
    private Integer rating;
    
    /*
    This variable is not used anywhere.
    It must be defined here to be able to receive rating request.
    Without it, it will throw HttpStatus 400 :)
    It is some kind of dark magic.
    */
    private LocalDateTime time;
    
}
