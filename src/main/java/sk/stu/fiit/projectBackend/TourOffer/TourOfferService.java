/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourOffer;

import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sk.stu.fiit.projectBackend.TourOffer.dto.CreateTourOfferRequest;
import sk.stu.fiit.projectBackend.TourOffer.dto.CreateTourOfferResponse;
import sk.stu.fiit.projectBackend.User.AppUser;
import sk.stu.fiit.projectBackend.User.AppUserRepository;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class TourOfferService {

    private final TourOfferRepository tourOfferRepository;
    private final AppUserRepository appUserRepository;

    @Transactional
    public CreateTourOfferResponse createTourOffer(
            CreateTourOfferRequest request) {

        String userEmail = SecurityContextHolder.getContext().
                getAuthentication().getName();

        AppUser user = appUserRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalStateException("Fatal error, user not found")
        );

        TourOffer newOffer = new TourOffer(request.getStartPlace(), request.
                getDestinationPlace(), request.getDescription(), request.
                getPricePerPerson());

        user.getTourOffers().add(newOffer);

        TourOffer savedOffer = tourOfferRepository.save(newOffer);

        return new CreateTourOfferResponse(savedOffer, user.getId());
    }

}
