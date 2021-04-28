/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Utils;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import static sk.stu.fiit.projectBackend.Other.Constants.USER_NOT_FOUND;
import sk.stu.fiit.projectBackend.User.AppUser;
import sk.stu.fiit.projectBackend.User.AppUserRepository;
import sk.stu.fiit.projectBackend.User.AppUserTypes;
import sk.stu.fiit.projectBackend.exceptions.PermissionDeniedException;

/**
 *
 * @author Adam Bublavý
 */
@Component
@AllArgsConstructor
public class AppUserUtils {

    private final AppUserRepository appUserRepository;

    /**
     * Fetch data about user who made the request
     *
     * @return Returns user's data
     *
     * @see AppUser
     */
    public AppUser getCurrentlyLoggedUser() {
        String userEmail = SecurityContextHolder.getContext().
                getAuthentication().getName();

        AppUser user = appUserRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalStateException(USER_NOT_FOUND)
        );

        return user;
    }

    /**
     * Checks if the user is guide.
     *
     * @param user
     *
     * @throws PermissionDeniedException
     */
    public void checkIfIsGuide(AppUser user) {
        if (user == null) {
            return;
        }

        if (user.getType() != AppUserTypes.GUIDE) {
            throw new PermissionDeniedException();
        }
    }

}
