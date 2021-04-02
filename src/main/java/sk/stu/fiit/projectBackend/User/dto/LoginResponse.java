/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.stu.fiit.projectBackend.User.AppUser;

/**
 *
 * @author Adam Bublavý
 */

@AllArgsConstructor
@Getter
public class LoginResponse {
    
    private final String jwtToken;
    private final AppUser user;
    
}
