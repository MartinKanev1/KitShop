package com.example.KitShop.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record FullOrderDTO(
        Long orderId,
        String status,
        String address,
        Long userTo,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        List<FullOrderItemDTO> items
) {}

