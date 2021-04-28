/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User.dto;

import java.time.LocalDate;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import sk.stu.fiit.projectBackend.validators.NullOrNotBlank;

/**
 *
 * @author Adam Bublavý
 */
@Data
@AllArgsConstructor
public class UpdateRequest {

    @NullOrNotBlank(message = "required")
    @Size(min = 8, message = "password must be at least 8 characters long")
    private String password;

    @NullOrNotBlank(message = "required")
    private String firstName;

    @NullOrNotBlank(message = "required")
    private String lastName;

    @PastOrPresent(message = "invalid range")
    private LocalDate dateOfBirth;

    private byte[] photo;

}
