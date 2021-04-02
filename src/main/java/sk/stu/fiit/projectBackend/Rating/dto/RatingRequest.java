/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Rating.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    
    @Min(value = 0, message = "value must be between 0 and 5")
    @Max(value = 5, message = "value must be between 0 and 5")
    private int rating;
    
}
