package com.example.KitShop.Repositories;

import com.example.KitShop.Models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository< Orders, Long> {
}
