/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import static sk.stu.fiit.projectBackend.Other.Constants.USER_NOT_FOUND;
import sk.stu.fiit.projectBackend.Rating.RatingRepository;
import sk.stu.fiit.projectBackend.TourOffer.TourOffer;
import sk.stu.fiit.projectBackend.TourOffer.TourOfferRepository;
import sk.stu.fiit.projectBackend.TourOffer.dto.TourOfferResponse;
import sk.stu.fiit.projectBackend.TourOffer.dto.TourOffersDataPage;
import sk.stu.fiit.projectBackend.User.dto.LoginRequest;
import sk.stu.fiit.projectBackend.User.dto.LoginResponse;
import sk.stu.fiit.projectBackend.User.dto.RegisterRequest;
import sk.stu.fiit.projectBackend.User.dto.RegisterResponse;
import sk.stu.fiit.projectBackend.User.dto.UpdateRequest;
import sk.stu.fiit.projectBackend.Utils.AppUserUtils;
import sk.stu.fiit.projectBackend.Utils.JWTUtil;
import sk.stu.fiit.projectBackend.exceptions.EmailTakenException;
import sk.stu.fiit.projectBackend.exceptions.IncorrectUsernameOrPasswordException;
import sk.stu.fiit.projectBackend.exceptions.UserNotFoundException;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final TourOfferRepository tourOfferRepository;
    private final RatingRepository ratingRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final AppUserUtils appUserUtils;

    /**
     * Finds user by the user's email
     *
     * @param email User's email
     * @return Returns UserDetails of the user
     * @throws UsernameNotFoundException
     *
     * @see AppUser
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws
            UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(String.
                        format(USER_NOT_FOUND, email)));
    }

    /**
     * Registers new user and logs in.
     *
     * @param request Data about user
     * @return Returns newly created AppUser mapped into RegisterResponse.
     *
     * @see AppUser
     * @see RegisterRequest
     * @see RegisterResponse
     */
    public RegisterResponse register(RegisterRequest request) {
        boolean userExists = appUserRepository.findByEmail(request.getEmail()).
                isPresent();

        if (userExists) {
            throw new EmailTakenException(request.getEmail());
        }

        String hashedPassword = bCryptPasswordEncoder.encode(request.
                getPassword());

        request.setPassword(hashedPassword);

        AppUser user = new AppUser(request.getEmail(), hashedPassword,
                AppUserTypes.valueOf(request.getType()), request.getFirstName(),
                request.getLastName(),
                request.getDateOfBirth(), request.getPhoto());

        appUserRepository.save(user);

        String jwtToken = jwtUtil.generateToken(user);

        return new RegisterResponse(jwtToken);
    }

    /**
     * Logs in the user.
     *
     * @param request Log in data from the user
     * @return Returns log in data mapped into LoginResponse
     *
     * @see AppUser
     * @see LoginRequest
     * @see LoginResponse
     */
    public LoginResponse login(LoginRequest request) {
        Optional<AppUser> userOptional = appUserRepository.findByEmail(
                request.getEmail());

        if (!userOptional.isPresent()) {
            throw new IncorrectUsernameOrPasswordException();
        }

        AppUser user = userOptional.get();

        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.
                getPassword())) {
            throw new IncorrectUsernameOrPasswordException();

        }

        String jwtToken = jwtUtil.generateToken(user);

        return new LoginResponse(jwtToken, user);
    }

    /**
     * @return Returns data about currently logged in user.
     *
     * @see AppUser
     */
    public AppUser me() {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        return user;
    }

    /**
     * Updates user's data
     *
     * @param request Update data sent by user
     * @return Returns updated AppUser
     *
     * @see AppUser
     * @see UpdateRequest
     */
    public AppUser updateUser(UpdateRequest request) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();
        int hashBeforeUpdate = user.hashCode();

        if (request.getPassword() != null) {
            String hashedPassword = bCryptPasswordEncoder.encode(request.
                    getPassword());
            user.setPassword(hashedPassword);
        }
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getPhoto() != null) {
            user.setPhoto(request.getPhoto());
        }

        if (hashBeforeUpdate != user.hashCode()) {
            user.setUpdatedAt(LocalDateTime.now());
        }

        appUserRepository.save(user);

        return user;
    }

    /**
     * Gets data about specific user
     *
     * @param userId ID of the user
     * @return Returns data about specific user
     *
     * @see AppUser
     */
    public AppUser getUser(UUID userId) {
        AppUser user = appUserRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId));

        return user;
    }

    /**
     * Gets TourOffers created by the user
     *
     * @param page Data about pagination
     * @return Returns Page of user's TourOffers
     *
     * @see TourOffer
     * @see AppUser
     * @see Page
     */
    public Page<TourOfferResponse> getUsersTourOffers(TourOffersDataPage page) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();

        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        Pageable pageable = PageRequest.of(page.getPageNumber(), page.
                getPageSize(), sort);

        Page<TourOffer> userOffers = tourOfferRepository.findAllByUserId(user.
                getId(), pageable);

        Page<TourOfferResponse> transformedUserOffers = userOffers.map(
                TourOfferResponse::new);

        transformedUserOffers.stream().forEach(e -> {
            double averageRating = ratingRepository.calculateAverageRating(
                    e.getId()).orElse(-1.0);
            e.setAverageRating(averageRating);
        });

        return transformedUserOffers;
    }

}
