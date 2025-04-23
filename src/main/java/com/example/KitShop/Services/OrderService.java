package com.example.KitShop.Services;

import com.example.KitShop.DTOs.FullOrderDTO;
import com.example.KitShop.DTOs.OrderItemDTO;
import com.example.KitShop.DTOs.OrdersDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    OrdersDTO buyProduct(Long userId, Long productId, String size, int quantity, String shippingAddress);

    List<OrderItemDTO> getOrderItemsByOrderId(Long orderId);

    List<OrdersDTO> getAllOrders();

    void deleteOrder(Long orderId);

    OrdersDTO approveOrder(Long orderId);

    void cancelOrder(Long orderId);

    List<FullOrderDTO> getAllOrdersWithDetails();

    List<FullOrderDTO> getAllOrdersWithDetailsByUserId(Long userId);

    OrdersDTO checkout(Long userId, String shippingAddress);
}
