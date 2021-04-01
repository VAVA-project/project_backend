/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sk.stu.fiit.projectBackend.Cart.CartTicket;
import sk.stu.fiit.projectBackend.Order.UserOrder;
import sk.stu.fiit.projectBackend.TourDate.Ticket;
import sk.stu.fiit.projectBackend.TourOffer.TourOffer;

/**
 *
 * @author Adam Bublavý
 */
@Data
@NoArgsConstructor
@Entity
@Table
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid_user")
    @GenericGenerator(
            name = "uuid_user",
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

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppUserTypes type;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    private byte[] photo;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "user"
    )
    private List<TourOffer> tourOffers = new ArrayList<>(0);

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "user",
            orphanRemoval = true
    )
    private List<CartTicket> cartTickets = new ArrayList<>(0);

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "user"
    )
    private List<UserOrder> orders = new ArrayList<>(0);

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "user"
    )
    private List<Ticket> tickets = new ArrayList<>(0);

    public AppUser(String email, String password, AppUserTypes type,
            String firstName, String lastName, LocalDate dateOfBirth,
            byte[] photo) {
        this.email = email;
        this.password = password;
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.photo = photo;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void addTourOffer(TourOffer tourOffer) {
        if (tourOffer == null) {
            return;
        }

        tourOffers.add(tourOffer);
        tourOffer.setUser(this);
    }

    public void removeTourOffer(TourOffer tourOffer) {
        if (tourOffer == null) {
            return;
        }

        tourOffers.remove(tourOffer);
        tourOffer.setUser(null);
    }

    public void addCartTicket(CartTicket cartTicket) {
        if (cartTicket == null) {
            return;
        }

        cartTickets.add(cartTicket);
        cartTicket.setUser(this);
    }

    public void removeCartTicket(CartTicket cartTicket) {
        if (cartTicket == null) {
            return;
        }

        cartTickets.remove(cartTicket);
        cartTicket.setUser(null);
    }

    public void addTicket(Ticket ticket) {
        if (ticket == null) {
            return;
        }

        tickets.add(ticket);
        ticket.setUser(this);
    }

    public void removeTicket(Ticket ticket) {
        if (ticket == null) {
            return;
        }

        ticket.setLockExpiresAt(null);
        tickets.remove(ticket);
        ticket.setUser(null);
    }
    
    public void addOrder(UserOrder order) {
        if(order == null) return;
        
        orders.add(order);
        order.setUser(this);
    }
    
    public void removeOrder(UserOrder order) {
        if(order == null) return;
        
        orders.remove(order);
        order.setUser(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(type.
                name());
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
