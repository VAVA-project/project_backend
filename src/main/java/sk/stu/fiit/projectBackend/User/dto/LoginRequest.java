/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Adam Bublavý
 */

@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {
    
    @NotBlank(message = "required")
    @Email(message = "invalid email")
    private String email;
    
    @NotBlank(message = "required")
    private String password;
    
}
