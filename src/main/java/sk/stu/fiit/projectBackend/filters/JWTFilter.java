/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sk.stu.fiit.projectBackend.User.AppUserService;
import sk.stu.fiit.projectBackend.Utils.JWTUtil;

/**
 *
 * @author Adam Bublavý
 */
@Component
@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private static final int JWT_PREFIX = 7;

    private final JWTUtil jwtUtil;
    private final AppUserService appUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain fc) throws
            ServletException,
            IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            fc.doFilter(request, response);
            return;
        }

        String jwt = authorization.substring(JWT_PREFIX);
        String email = jwtUtil.extractUsernameFromToken(jwt);
        
        if (email != null && SecurityContextHolder.getContext().
                getAuthentication() == null) {
            UserDetails userDetails = this.appUserService.loadUserByUsername(
                    email);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(
                                request));

                SecurityContextHolder.getContext().setAuthentication(
                        usernamePasswordAuthenticationToken);
            }

        }

        fc.doFilter(request, response);
    }

}
