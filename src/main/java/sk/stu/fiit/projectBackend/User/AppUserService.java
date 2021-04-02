/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import static sk.stu.fiit.projectBackend.Other.Constants.USER_NOT_FOUND;
import sk.stu.fiit.projectBackend.User.dto.LoginRequest;
import sk.stu.fiit.projectBackend.User.dto.LoginResponse;
import sk.stu.fiit.projectBackend.User.dto.RegisterRequest;
import sk.stu.fiit.projectBackend.User.dto.RegisterResponse;
import sk.stu.fiit.projectBackend.User.dto.UpdateRequest;
import sk.stu.fiit.projectBackend.Utils.AppUserUtils;
import sk.stu.fiit.projectBackend.Utils.JWTUtil;
import sk.stu.fiit.projectBackend.exceptions.EmailTakenException;
import sk.stu.fiit.projectBackend.exceptions.IncorrectUsernameOrPasswordException;

/**
 *
 * @author Adam Bublavý
 */
@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final AppUserUtils appUserUtils;

    @Override
    public UserDetails loadUserByUsername(String email) throws
            UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(String.
                        format(USER_NOT_FOUND, email)));
    }
    
    public RegisterResponse register(RegisterRequest request) {
        boolean userExists = appUserRepository.findByEmail(request.getEmail()).
                isPresent();

        if (userExists) {
            throw new EmailTakenException();
        }

        String hashedPassword = bCryptPasswordEncoder.encode(request.getPassword());

        request.setPassword(hashedPassword);
        
        AppUser user = new AppUser(request.getEmail(), hashedPassword,
                AppUserTypes.valueOf(request.getType()), request.getFirstName(), request.getLastName(),
                request.getDateOfBirth(), request.getPhoto());

        appUserRepository.save(user);
        
        String jwtToken = jwtUtil.generateToken(user);
        
        return new RegisterResponse(jwtToken);
    }
    
    public LoginResponse login(LoginRequest request) {
        Optional<AppUser> userOptional = appUserRepository.findByEmail(
                request.getEmail());
        
        if(!userOptional.isPresent()) {
            throw new IncorrectUsernameOrPasswordException();
        }
        
        AppUser user = userOptional.get();
        
        if(!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IncorrectUsernameOrPasswordException();
            
        }
        
        String jwtToken = jwtUtil.generateToken(user);
        
        return new LoginResponse(jwtToken);
    }
    
    public AppUser me() {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();
        
        return user;
    }
    
    public AppUser updateUser(UpdateRequest request) {
        AppUser user = appUserUtils.getCurrentlyLoggedUser();
        int hashBeforeUpdate = user.hashCode();
        
        if(request.getPassword() != null) {
            String hashedPassword = bCryptPasswordEncoder.encode(request.getPassword());
            user.setPassword(hashedPassword);
        }
        if(request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if(request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if(request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth());
        }
        if(request.getPhoto() != null) {
            user.setPhoto(request.getPhoto());
        }
        
        if(hashBeforeUpdate != user.hashCode()) {
            user.setUpdatedAt(LocalDateTime.now());
        }
        
        appUserRepository.save(user);
        
        return user;
    }
    
}
