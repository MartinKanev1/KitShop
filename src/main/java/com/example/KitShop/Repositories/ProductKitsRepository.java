package com.example.KitShop.Repositories;

import com.example.KitShop.Models.ProductKits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductKitsRepository extends JpaRepository<ProductKits, Long> {
}
