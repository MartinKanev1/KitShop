package com.example.KitShop.Repositories;

import com.example.KitShop.Models.ProductKitSizeQuantities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductKitSizeQuantitiesRepository extends JpaRepository<ProductKitSizeQuantities, Long> {
    //Optional<Object> findByProductKitIdAndSize(Long productId, String size);

    Optional<ProductKitSizeQuantities> findByProductKit_ProductKitIdAndSize(Long productKitId, String size);

}
