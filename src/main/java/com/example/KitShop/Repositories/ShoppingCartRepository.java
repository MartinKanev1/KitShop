package com.example.KitShop.Repositories;

import com.example.KitShop.Models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository< ShoppingCart, Long> {
}
