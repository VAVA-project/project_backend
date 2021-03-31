/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourDate.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import sk.stu.fiit.projectBackend.TourDate.TourDate;

/**
 *
 * @author Adam Bublavý
 */
@Data
@AllArgsConstructor
public class TourDateResponse {
    
    private UUID id;
    private UUID tourOfferId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int numberOfTickets;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public TourDateResponse(TourDate tourDate, UUID tourOfferId) {
        this.id = tourDate.getId();
        this.tourOfferId = tourOfferId;
        this.startDate = tourDate.getStartDate();
        this.endDate = tourDate.getEndDate();
        this.numberOfTickets = tourDate.getTickets().size();
        this.createdAt = tourDate.getCreatedAt();
        this.updatedAt = tourDate.getUpdatedAt();
    }
    
}
