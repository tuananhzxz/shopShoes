package com.example.ShopShoes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private int status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String guestEmail;

    @Column(name = "phone")
    private String guestPhone;

    @Column(name = "address")
    private String guestAddress;

    @Column(name = "city")
    private String city;

    @Column(name = "company")
    private String company;

    @Column(name = "state")
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "message")
    private String message;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "payment")
    private String payment;

    @Column(name = "created")
    private Timestamp created;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @PrePersist
    protected void onCreate() {
        this.created = Timestamp.valueOf(LocalDateTime.now());
        this.status = 1;
    }
}
