package com.example.ShopShoes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "card_items")
public class CardItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_item_id")
    private Long cardItemId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "added_at")
    private Timestamp addedAt;

    @ManyToOne
    @JoinColumn(name = "shipping_option_id", referencedColumnName = "shipping_option_id")
    private ShippingOption shippingOption;

    @PrePersist
    protected void onCreate() {
        this.addedAt = Timestamp.valueOf(LocalDateTime.now());
    }
}
