/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.UserOrder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Adam Bublavý
 */
@Getter
@Setter
@Entity
@Table
@ToString
public class UserOrder {

    @Id
    @GeneratedValue(generator = "uuid_user_order")
    @GenericGenerator(
            name = "uuid_user_order",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
            updatable = false,
            nullable = false
    )
    private UUID id;

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime orderTime;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(
            nullable = false, 
            updatable = false
    )
    private double totalPrice;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "orderId",
            referencedColumnName = "id",
            nullable = false,
            updatable = false
    )
    private List<OrderTicket> orderedTickets = new ArrayList<>(0);

    public UserOrder(LocalDateTime orderTime, double totalPrice) {
        this.orderTime = orderTime;
        this.totalPrice = totalPrice;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
