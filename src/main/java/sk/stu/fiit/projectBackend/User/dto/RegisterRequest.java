/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User.dto;

import java.time.LocalDate;
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
    
    private String email;
    private String password;
    private AppUserTypes type;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    
}
