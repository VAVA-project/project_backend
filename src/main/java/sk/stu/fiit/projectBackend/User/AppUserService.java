/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sk.stu.fiit.projectBackend.exceptions.EmailTakenException;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private static final String USER_NOT_FOUND = "User with email %s not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws
            UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(String.
                        format(USER_NOT_FOUND, email)));
    }

    public AppUser register(AppUser user) {
        boolean userExists = appUserRepository.findByEmail(user.getEmail()).
                isPresent();

        if (userExists) {
            throw new EmailTakenException("Email already taken");
        }

        String hashedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(hashedPassword);

        appUserRepository.save(user);
        
        return user;
    }

}
