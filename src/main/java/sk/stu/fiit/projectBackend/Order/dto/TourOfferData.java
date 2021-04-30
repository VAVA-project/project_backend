/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Order.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Adam Bublavý
 */
@Data
@AllArgsConstructor
public class TourOfferData {
    private UUID id;
    
    private String startPlace;
    private String destinationPlace;
    private String description;
    
    private Integer rating;
    private Double averageRating;
}
