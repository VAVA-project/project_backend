/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourDate;

import java.util.UUID;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.stu.fiit.projectBackend.TourDate.dto.CreateTourDateRequest;
import sk.stu.fiit.projectBackend.TourDate.dto.TourDateDataPage;
import sk.stu.fiit.projectBackend.TourDate.dto.TourDateResponse;
import sk.stu.fiit.projectBackend.TourDate.dto.UpdateTourDateRequest;

/**
 *
 * @author Adam Bublavý
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/tours/{tourOfferId}")
public class TourDateController {

    private final TourDateService tourDateService;

    @PostMapping(path = "/dates/",
            consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<TourDateResponse> createTourDate(
            @PathVariable("tourOfferId") UUID id,
            @Valid @RequestBody CreateTourDateRequest request) {
        return ResponseEntity.ok(tourDateService.createTourDate(id, request));
    }

    @PutMapping(path = "/dates/{tourDateId}/",
            consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<TourDateResponse> updateTourDate(
            @PathVariable("tourOfferId") UUID tourOfferId,
            @PathVariable("tourDateId") UUID tourDateId,
            @Valid @RequestBody UpdateTourDateRequest request) {
        return ResponseEntity.ok(tourDateService.updateTourDate(tourOfferId,
                tourDateId, request));
    }

    @DeleteMapping(path = "/dates/{tourDateId}/")
    public ResponseEntity<Object> deleteTourDate(
            @PathVariable("tourOfferId") UUID tourOfferId,
            @PathVariable("tourDateId") UUID tourDateId
    ) {
        HttpStatus status = tourDateService.deleteTourDate(tourOfferId,
                tourDateId);
        return ResponseEntity.status(status).build();
    }

    @GetMapping(path = "/dates/")
    public ResponseEntity<Page<TourDateResponse>> getTourDates(
            @PathVariable("tourOfferId") UUID tourOfferId,
            @Valid TourDateDataPage page
    ) {
        return ResponseEntity.
                ok(tourDateService.getTourDates(tourOfferId, page));
    }

}
