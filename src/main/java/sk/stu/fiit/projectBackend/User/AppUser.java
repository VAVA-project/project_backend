/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
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
import sk.stu.fiit.projectBackend.Rating.Rating;
import sk.stu.fiit.projectBackend.Ticket.Ticket;
import sk.stu.fiit.projectBackend.TourOffer.TourOffer;
import sk.stu.fiit.projectBackend.Views.Views;

/**
 * AppUser represents user of this application.
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
    @JsonView(Views.Public.class)
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
    @JsonView(Views.Public.class)
    private String firstName;

    @Column(nullable = false)
    @JsonView(Views.Public.class)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @JsonView(Views.Public.class)
    private byte[] photo;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "user"
    )
    @JsonIgnore
    private List<TourOffer> tourOffers = new ArrayList<>(0);

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "user",
            orphanRemoval = true
    )
    @JsonIgnore
    private List<CartTicket> cartTickets = new ArrayList<>(0);

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "user"
    )
    @JsonIgnore
    private List<UserOrder> orders = new ArrayList<>(0);

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "user"
    )
    @JsonIgnore
    private List<Ticket> tickets = new ArrayList<>(0);

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "user"
    )
    @JsonIgnore
    private List<Rating> ratings = new ArrayList<>(0);

    /**
     * Creates new AppUser
     *
     * @param email User's email
     * @param password User's password
     * @param type Type of the user
     * @param firstName User's firstname
     * @param lastName User's lastname
     * @param dateOfBirth User's date of birth
     * @param photo User's avatar
     *
     * @see AppUserTypes
     */
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

    /**
     * Adds new TourOffer for this user. It means that the user will be creator
     * of that TourOffer
     *
     * @param tourOffer TourOffer which will be added
     *
     * @see TourOffer
     */
    public void addTourOffer(TourOffer tourOffer) {
        if (tourOffer == null) {
            return;
        }

        tourOffers.add(tourOffer);
        tourOffer.setUser(this);
    }

    /**
     * Removes TourOffer for this user. It means that the user will be removed
     * as creator of that TourOffer
     *
     * @param tourOffer TourOffer which will be removed
     *
     * @see TourOffer
     */
    public void removeTourOffer(TourOffer tourOffer) {
        if (tourOffer == null) {
            return;
        }

        tourOffers.remove(tourOffer);
        tourOffer.setUser(null);
    }

    /**
     * Adds cartTicket to the user's cart.
     *
     * @param cartTicket CartTicket which will be added to the user's cart
     *
     * @see CartTicket
     */
    public void addCartTicket(CartTicket cartTicket) {
        if (cartTicket == null) {
            return;
        }

        cartTickets.add(cartTicket);
        cartTicket.setUser(this);
    }

    /**
     * Removes cartTicket from the user's cart.
     *
     * @param cartTicket CartTicket which will be removed from the user's cart
     *
     * @see CartTicket
     */
    public void removeCartTicket(CartTicket cartTicket) {
        if (cartTicket == null) {
            return;
        }

        cartTickets.remove(cartTicket);
        cartTicket.setUser(null);
    }

    /**
     * Adds ticket to this user. It means that the user will lock the ticket for
     * specific amount of time to prevent other user's to lock that ticket.
     *
     * @param ticket Ticket which will be added
     *
     * @see Ticket
     */
    public void addTicket(Ticket ticket) {
        if (ticket == null) {
            return;
        }

        tickets.add(ticket);
        ticket.setUser(this);
        ticket.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Removes ticket from this user. It means that the user will unlock the
     * ticket for other users.
     *
     * @param ticket Tichet which will be removed
     *
     * @see Ticket
     */
    public void removeTicket(Ticket ticket) {
        if (ticket == null) {
            return;
        }

        ticket.setLockExpiresAt(null);
        ticket.setUpdatedAt(LocalDateTime.now());
        tickets.remove(ticket);
        ticket.setUser(null);
    }

    /**
     * Adds new order to this user.
     *
     * @param order UserOrder which will be added to this user
     *
     * @see UserOrder
     */
    public void addOrder(UserOrder order) {
        if (order == null) {
            return;
        }

        orders.add(order);
        order.setUser(this);
    }

    /**
     * Removes the order from this user
     *
     * @param order UserOrder which will be removed from this user
     *
     * @see UserOrder
     */
    public void removeOrder(UserOrder order) {
        if (order == null) {
            return;
        }

        orders.remove(order);
        order.setUser(null);
    }

    /**
     * Adds rating to this user
     *
     * @param rating Rating which will be added to this user
     *
     * @see Rating
     */
    public void addRating(Rating rating) {
        if (rating == null) {
            return;
        }

        this.ratings.add(rating);
        rating.setUser(this);
    }

    /**
     * Removes rating from this user
     *
     * @param rating Rating which will be removed from this user
     *
     * @see Rating
     */
    public void removeRating(Rating rating) {
        if (rating == null) {
            return;
        }

        this.ratings.remove(rating);
        rating.setUser(null);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(type.
                name());
        return Collections.singletonList(authority);
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
