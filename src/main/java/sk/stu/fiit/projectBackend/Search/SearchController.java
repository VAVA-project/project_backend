/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Search;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sk.stu.fiit.projectBackend.TourOffer.TourOffer;
import sk.stu.fiit.projectBackend.TourOffer.dto.DataPage;
import sk.stu.fiit.projectBackend.TourOffer.dto.TourOfferResponse;

/**
 *
 * @author Adam Bublavý
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1")
public class SearchController {
    
    private final SearchService searchService;
    
    @GetMapping(path = "/search/")
    public ResponseEntity<Page<TourOfferResponse>> searchTourOffers(
            @RequestParam("q") String query,
            @Valid DataPage page
    ) {
        Page<TourOffer> offers = searchService.searchTourOffers(query, page);
        List<TourOfferResponse> mappedOffers = offers.getContent().stream().map(
                TourOfferResponse::new).collect(Collectors.toList());
        
        Page<TourOfferResponse> response = new PageImpl<>(mappedOffers, offers.getPageable(), mappedOffers.size());
        
        return ResponseEntity.ok(response);
    }
    
}
