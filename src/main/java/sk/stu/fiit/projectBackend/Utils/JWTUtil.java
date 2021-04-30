/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sk.stu.fiit.projectBackend.User.AppUser;

/**
 * JWTUtil is util which is used to work with jwt tokens
 *
 * Based on https://www.youtube.com/watch?v=X80nJ5T7YpE
 *
 * @author Adam Bublavý
 */
@Component
public class JWTUtil implements Serializable {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private static final long JWT_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60;

    /**
     * Extracts specific claim from the token
     *
     * @param <T> Claim data type
     * @param token Token
     * @param claimsResolver Function which is used to extract the claim
     * @return Returns extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).
                getBody();
    }

    public String extractUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpirationDateFromToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpirationDateFromToken(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(userDetails.getUsername(), claims);
    }

    /**
     * Creates new token
     *
     * @param subject Subject of the token
     * @param claims Additional data
     * @return Returns newly created token
     */
    private String createToken(String subject, Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(
                new Date(System.currentTimeMillis())).setExpiration(new Date(
                        System.currentTimeMillis() + JWT_TOKEN_EXPIRATION_TIME)).
                signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    /**
     * Validates token by comparing username saved in the token with username in
     * userDetails
     *
     * @param token Token which will be validated
     * @param userDetails Users data which will be compared with data saved in
     * this token
     * @return Returns true, if the token is valid, false otherwise
     *
     * @see AppUser
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsernameFromToken(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

}
