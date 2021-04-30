/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Cart.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import sk.stu.fiit.projectBackend.Cart.CartTicket;

/**
 *
 * @author Adam Bublavý
 */
@Data
@AllArgsConstructor
public class CartResponse {
    
    private List<CartTicket> tickets;
    private double totalPrice;
    
}
