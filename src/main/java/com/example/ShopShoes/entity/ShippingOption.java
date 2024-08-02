package com.example.ShopShoes.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "shipping_options")
public class ShippingOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_option_id")
    private Long shippingOptionId;

    @Column(name = "name")
    private String name;

    @Column(name = "fee")
    private BigDecimal fee;

}
