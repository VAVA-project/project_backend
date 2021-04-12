/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stu.fiit.projectBackend.validators.NullOrNotBlank;

/**
 *
 * @author Adam Bublavý
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutRequest {
    
    @NullOrNotBlank
    private String comments;
    
}
