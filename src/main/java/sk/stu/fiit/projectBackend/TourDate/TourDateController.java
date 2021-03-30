/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourDate;

import java.util.UUID;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.stu.fiit.projectBackend.TourDate.dto.CreateTourDateRequest;
import sk.stu.fiit.projectBackend.TourDate.dto.TourDateResponse;

/**
 *
 * @author Adam Bublavý
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/tourOffer")
public class TourDateController {

    private final TourDateService tourDateService;

    @PostMapping(path = "/{tourOfferId}",
            consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<TourDateResponse> createTourDate(@PathVariable(
            "tourOfferId") UUID id,
            @Valid @RequestBody CreateTourDateRequest request) {
        return ResponseEntity.ok(tourDateService.createTourDate(id, request));
    }

}
