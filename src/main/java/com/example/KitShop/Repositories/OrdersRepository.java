package com.example.KitShop.Repositories;

import com.example.KitShop.DTOs.OrdersDTO;
import com.example.KitShop.Models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository< Orders, Long> {
    //List<OrdersDTO> findAllByUser_UserId(Long userId);

//    @Query("""
//    SELECT new com.example.KitShop.DTOs.OrdersDTO(
//        o.id,
//        o.user.userId,
//        o.orderItems,
//        o.status,
//        o.address,
//        o.totalAmount,
//        o.createdAt
//    )
//    FROM Orders o
//    WHERE o.user.userId = :userId
//""")
//    List<OrdersDTO> findAllOrdersByUser(Long userId);

    List<Orders> findByUser_UserId(Long userId);


    @Query("SELECT o.user.id FROM Orders o WHERE o.orderId = :orderId")
    Long findUserIdByOrderId( Long orderId);
}
