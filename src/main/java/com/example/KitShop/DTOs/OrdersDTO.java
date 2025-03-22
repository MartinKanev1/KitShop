package com.example.KitShop.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdersDTO(
        Long orderId,
        Long userId,
        List<OrderItemDTO> orderItems,
        String status,
        String address,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {
}
