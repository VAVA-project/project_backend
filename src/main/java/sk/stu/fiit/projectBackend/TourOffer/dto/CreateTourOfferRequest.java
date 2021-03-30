/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourOffer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
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
public class CreateTourOfferRequest {
    
    @NotBlank(message = "required")
    private String startPlace;
    
    @NotBlank(message = "required")
    private String destinationPlace;
    
    @NotBlank(message = "required")
    private String description;
    
    @Positive(message = "price must be positive")
    private double pricePerPerson;
    
}
