/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourOffer.dto;

import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Adam Bublavý
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateTourOfferRequest {
    
    private String startPlace;
    private String destinationPlace;
    private String description;
    
    @Positive(message = "price must be positive")
    private Double pricePerPerson;
}
