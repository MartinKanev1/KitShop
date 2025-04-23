package com.example.KitShop.Repositories;

import com.example.KitShop.DTOs.OrdersDTO;
import com.example.KitShop.Models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository< Orders, Long> {


    List<Orders> findByUser_UserId(Long userId);


    @Query("SELECT o.user.id FROM Orders o WHERE o.orderId = :orderId")
    Long findUserIdByOrderId( Long orderId);
}
