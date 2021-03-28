/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Adam Bublavý
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
            updatable = false,
            nullable = false
    )
    private UUID id;
    
    @Column(
            unique = true,
            nullable = false
    )
    private String email;
    
    @Column(
            nullable = false
    )
    private String password;
    
    @Column(
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private AppUserTypes type;
    
    @Column(
            nullable = false
    )
    private String firstName;
    
    @Column(
            nullable = false
    )
    private String lastName;
    
    @Column(
            nullable = false
    )
    private LocalDate dateOfBirth;
    
    private byte[] photo;
    
    @Column(
            nullable = false
    )
    private LocalDate createdAt;
    
    @Column(
            nullable = false
    )
    private LocalDate updatedAt;
    
    @Column(
            nullable = false
    )
    private LocalDate deletedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(type.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
}
