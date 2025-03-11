package com.example.KitShop.Repositories;

import com.example.KitShop.Models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository< CartItem, Long> {
}
