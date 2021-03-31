/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.Order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import sk.stu.fiit.projectBackend.User.AppUser;

/**
 *
 * @author Adam Bublavý
 */
@Data
@NoArgsConstructor
@Entity
@Table
public class Order {
    
    @Id
    @GeneratedValue(generator = "uuid_order")
    @GenericGenerator(
            name = "uuid_order",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
            updatable = false,
            nullable = false
    )
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "userId",
            referencedColumnName = "id",
            nullable = false
    )
    @JsonIgnore
    private AppUser user;
    
    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime orderTime;
    
    @Column(
            nullable = false,
            updatable = false
    )
    private double totalPrice;
    
    private String comments;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "order"
    )
    private List<OrderTicket> orderTickets = new ArrayList<>(0);
    
    public Order(AppUser user, LocalDateTime orderTime, double totalPrice,
            String comments) {
        this.user = user;
        this.orderTime = orderTime;
        this.totalPrice = totalPrice;
        this.comments = comments;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addOrderTicket(OrderTicket orderTicket) {
        if (orderTicket == null) {
            return;
        }
        
        this.orderTickets.add(orderTicket);
        orderTicket.setOrder(this);
    }
    
    public void removeOrderTicket(OrderTicket orderTicket) {
        if (orderTicket == null) {
            return;
        }
        
        this.orderTickets.remove(orderTicket);
        orderTicket.setOrder(null);
    }
    
}
