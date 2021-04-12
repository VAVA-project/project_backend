/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Rating;

import java.util.UUID;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.stu.fiit.projectBackend.Rating.dto.RatingRequest;

/**
 *
 * @author Adam Bublavý
 */
@RestController
@RequestMapping(path = "api/v1/rating")
@AllArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping(path = "/{tourOfferId}",
            consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> addRating(
            @PathVariable(name = "tourOfferId") UUID tourOfferId,
            @Valid @RequestBody RatingRequest request) {
        HttpStatus status = ratingService.addRating(tourOfferId, request);
        return ResponseEntity.status(status).build();
    }

}
