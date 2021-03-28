/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User.dto;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sk.stu.fiit.projectBackend.User.AppUserTypes;

/**
 *
 * @author Adam Bublavý
 */
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    
    @NotNull(message = "required")
    @Email(message = "invalid email")
    private String email;
    
    @NotNull(message = "required")
    @Size(min = 8, message = "password must be at least 8 characters long")
    private String password;
    
    @NotNull(message = "required")
    private AppUserTypes type;
    
    @NotBlank(message = "required")
    private String firstName;
    
    @NotBlank(message = "required")
    private String lastName;
    
    @NotNull(message = "required")
    @PastOrPresent(message = "invalid range")
    private LocalDate dateOfBirth;
    
    private byte[] photo;
    
}
