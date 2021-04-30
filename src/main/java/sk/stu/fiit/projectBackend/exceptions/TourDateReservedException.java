/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

import sk.stu.fiit.projectBackend.Other.Constants;

/**
 * TourDateReservedException is thrown when user wants to delete TourDate for
 * which somebody has already bought ticket
 *
 * @author Adam Bublavý
 */
public class TourDateReservedException extends RuntimeException {

    public TourDateReservedException() {
        super(Constants.TOUR_DATE_DELETE_RESERVED);
    }

}
