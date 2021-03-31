/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.TourOffer.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Data;
import org.springframework.data.domain.Sort;
import sk.stu.fiit.projectBackend.validators.In;
import sk.stu.fiit.projectBackend.validators.NullOrNotBlank;

/**
 *
 * @author Adam Bublavý
 */
@Data
public class TourOfferPage {

    @Min(0)
    private int pageNumber = 0;

    @Min(0)
    @Max(20)
    private int pageSize = 10;

    private Sort.Direction sortDirection = Sort.Direction.DESC;

    @NullOrNotBlank
    @In(allowedValues = {"id", "createdAt", "updatedAt", "deletedAt"})
    private String sortBy = "createdAt";

}
