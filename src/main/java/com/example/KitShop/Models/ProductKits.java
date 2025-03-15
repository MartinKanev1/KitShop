package com.example.KitShop.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Data
@Entity
@Setter
@Getter
@Table(name = "product_kits")
public class ProductKits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productKitId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String playerNameOnKit;

    @Column(nullable = false)
    private String teamNameOfKit;


    @Column(name = "kit_size")
    private String size;

    @Enumerated(EnumType.STRING)
    @Column(name = "club_type")
    private ClubType type;

    @Column(name = "stock_quantity", nullable = false)
    private int quantity;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reviews> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

}
