package com.example.KitShop.Repositories;

import com.example.KitShop.Models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository< OrderItem, Long> {
}
