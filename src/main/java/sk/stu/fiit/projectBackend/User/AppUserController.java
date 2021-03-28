/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.stu.fiit.projectBackend.User.dto.LoginRequest;
import sk.stu.fiit.projectBackend.User.dto.LoginResponse;
import sk.stu.fiit.projectBackend.User.dto.RegisterRequest;
import sk.stu.fiit.projectBackend.User.dto.RegisterResponse;

/**
 *
 * @author Adam Bublavý
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1")
public class AppUserController {

    private final AppUserService appUserService;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(appUserService.register(request));
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {
        
        return ResponseEntity.ok(appUserService.login(request));
    }

}