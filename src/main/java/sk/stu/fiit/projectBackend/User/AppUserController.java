/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.UUID;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.stu.fiit.projectBackend.TourOffer.dto.TourOfferResponse;
import sk.stu.fiit.projectBackend.TourOffer.dto.TourOffersDataPage;
import sk.stu.fiit.projectBackend.User.dto.LoginRequest;
import sk.stu.fiit.projectBackend.User.dto.LoginResponse;
import sk.stu.fiit.projectBackend.User.dto.RegisterRequest;
import sk.stu.fiit.projectBackend.User.dto.RegisterResponse;
import sk.stu.fiit.projectBackend.User.dto.UpdateRequest;
import sk.stu.fiit.projectBackend.Views.Views;

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

    @GetMapping(path = "/users/{userId}/")
    @JsonView(Views.Public.class)
    public ResponseEntity<AppUser> getUser(
            @PathVariable("userId") UUID userId
    ) {
        return ResponseEntity.ok(appUserService.getUser(userId));
    }

    @GetMapping(path = "/users/tours/")
    public ResponseEntity<Page<TourOfferResponse>> getUsersTourOffers(
            @Valid TourOffersDataPage page) {
        return new ResponseEntity<>(appUserService.getUsersTourOffers(page),
                HttpStatus.OK);
    }

}
