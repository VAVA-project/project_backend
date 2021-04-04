/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourOffer.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import sk.stu.fiit.projectBackend.TourOffer.TourOffer;

/**
 *
 * @author Adam Bublavý
 */
@Data
@AllArgsConstructor
public class TourOfferResponse {
    
    private UUID id;
    private UUID creatorId;
    private String startPlace;
    private String destinationPlace;
    private String description;
    
    private double pricePerPerson;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TourOfferResponse(TourOffer offer) {
        this.id = offer.getId();
        this.creatorId = offer.getUser().getId();
        this.startPlace = offer.getStartPlace();
        this.destinationPlace = offer.getDestinationPlace();
        this.description = offer.getDescription();
        this.pricePerPerson = offer.getPricePerPerson();
        this.createdAt = offer.getCreatedAt();
        this.updatedAt = offer.getUpdatedAt();
    }
    
}
