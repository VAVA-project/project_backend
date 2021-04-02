/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.stu.fiit.projectBackend.TourOffer.TourOffer;
import sk.stu.fiit.projectBackend.TourOffer.dto.DataPage;
import sk.stu.fiit.projectBackend.User.dto.LoginRequest;
import sk.stu.fiit.projectBackend.User.dto.LoginResponse;
import sk.stu.fiit.projectBackend.User.dto.RegisterRequest;
import sk.stu.fiit.projectBackend.User.dto.RegisterResponse;
import sk.stu.fiit.projectBackend.User.dto.UpdateRequest;

/**
 *
 * @author Adam Bublavý
 */
@RestController
@AllArgsConstructor
@RequestMapping(
        path = "api/v1",
        consumes = MediaType.APPLICATION_XML_VALUE
)
public class AppUserController {

    private final AppUserService appUserService;

    @PostMapping(path = "/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(appUserService.register(request));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(appUserService.login(request));
    }

    @GetMapping(path = "/me")
    public ResponseEntity<AppUser> me() {
        return ResponseEntity.ok(appUserService.me());
    }

    @PutMapping(path = "/users/")
    public ResponseEntity<AppUser> updateUser(
            @Valid @RequestBody UpdateRequest request) {
        return ResponseEntity.ok(appUserService.updateUser(request));
    }

    @GetMapping(path = "/users/tours/")
    public ResponseEntity<Page<TourOffer>> getUsersTourOffers(
            @Valid DataPage page) {
        return new ResponseEntity<>(appUserService.getUsersTourOffers(page),
                HttpStatus.OK);
    }

}
