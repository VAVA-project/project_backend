/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Search;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sk.stu.fiit.projectBackend.Rating.RatingRepository;
import sk.stu.fiit.projectBackend.TourOffer.TourOffer;
import sk.stu.fiit.projectBackend.TourOffer.TourOfferRepository;
import sk.stu.fiit.projectBackend.TourOffer.dto.DataPage;
import sk.stu.fiit.projectBackend.TourOffer.dto.TourOfferResponse;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class SearchService {

    private final TourOfferRepository tourOfferRepository;
    private final RatingRepository ratingRepository;
    
    public Page<TourOfferResponse> searchTourOffers(String query, DataPage page) {
        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.
                getPageSize(), sort);

        Page<TourOffer> response = tourOfferRepository.findByDeletedAtIsNullAndStartPlaceContainingOrDestinationPlaceContaining(
                query, query,
                pageable);
        
        response.stream().forEach(e -> {
            double averageRating = ratingRepository.calculateAverageRating(
                    e.getId()).orElse(0.0);
            e.setAverageRating(averageRating);
        });
        
        Page<TourOfferResponse> transformedResponse = response.map(TourOfferResponse::new);
        
        return transformedResponse;
    }

}
