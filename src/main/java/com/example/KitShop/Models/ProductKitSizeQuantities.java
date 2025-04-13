package com.example.KitShop.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
//@Data
@Setter
@Getter
@Table(name = "product_kit_sizes")
public class ProductKitSizeQuantities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productKitSizeQuantityId;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_kit_id")
    private ProductKits productKit;

    @Override
    public String toString() {
        return "ProductKitSizeQuantities{" +
                "id=" + productKitSizeQuantityId +
                ", size=" + size +
                ", quantity=" + quantity +
                '}';
    }

}
