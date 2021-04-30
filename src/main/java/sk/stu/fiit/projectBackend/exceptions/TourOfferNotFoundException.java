/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

import java.util.UUID;
import sk.stu.fiit.projectBackend.Other.Constants;

/**
 * TourOfferNotFoundException is thrown when TourOffer with specific ID is not
 * found
 *
 * @author Adam Bublavý
 */
public class TourOfferNotFoundException extends RecordNotFoundException {

    public TourOfferNotFoundException(UUID id) {
        super(String.format(Constants.TOUR_OFFER_NOT_FOUND, id));
    }

}
