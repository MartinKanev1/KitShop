package com.example.KitShop.Services;

import com.example.KitShop.DTOs.OrderItemDTO;
import com.example.KitShop.DTOs.OrdersDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    OrdersDTO buyProduct(Long userId, Long productId, int quantity);

    List<OrderItemDTO> getOrderItemsByOrderId(Long orderId);

    List<OrdersDTO> getAllOrders();

    void deleteOrder(Long orderId);
}
