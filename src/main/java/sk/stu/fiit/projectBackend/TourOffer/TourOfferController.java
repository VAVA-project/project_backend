/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourOffer;

import java.util.UUID;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.stu.fiit.projectBackend.TourOffer.dto.CreateTourOfferRequest;
import sk.stu.fiit.projectBackend.TourOffer.dto.CreateTourOfferResponse;

/**
 *
 * @author Adam Bublavý
 */
@RestController
@RequestMapping(path = "api/v1/tourOffer")
@AllArgsConstructor
public class TourOfferController {

    private final TourOfferService tourOfferService;

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<CreateTourOfferResponse> createTourOffer(
            @Valid @RequestBody CreateTourOfferRequest request) {
        return ResponseEntity.ok(tourOfferService.createTourOffer(request));
    }

    @DeleteMapping(path = "/{tourOfferId}")
    public ResponseEntity<?> deleteTourOffer(
            @PathVariable(name = "tourOfferId") UUID id) {
        HttpStatus responseStatus = tourOfferService.deleteTourOffer(id) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND;

        return ResponseEntity.status(responseStatus).body(null);
    }

}
