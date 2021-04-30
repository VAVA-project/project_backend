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
import sk.stu.fiit.projectBackend.TourOffer.dto.TourOfferResponse;
import sk.stu.fiit.projectBackend.TourOffer.dto.TourOffersDataPage;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class SearchService {

    private final TourOfferRepository tourOfferRepository;
    private final RatingRepository ratingRepository;

    /**
     * Searches TourOffers which contains expression specified in query in their
     * start place or destination place.
     *
     * @param query Query specified by user which will be used to find
     * TourOffers
     * @param page Paging data
     * @return Returns paged TourOffers mapped in TourOfferResponse
     */
    public Page<TourOfferResponse> searchTourOffers(String query,
            TourOffersDataPage page) {
        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.
                getPageSize(), sort);

        Page<TourOffer> response = tourOfferRepository.
                findByDeletedAtIsNullAndStartPlaceContainingOrDestinationPlaceContaining(
                        query, query,
                        pageable);

        Page<TourOfferResponse> transformedResponse = response.map(
                TourOfferResponse::new);
        transformedResponse.stream().forEach(e -> {
            double averageRating = ratingRepository.calculateAverageRating(
                    e.getId()).orElse(-1.0);
            e.setAverageRating(averageRating);
        });

        return transformedResponse;
    }

}
