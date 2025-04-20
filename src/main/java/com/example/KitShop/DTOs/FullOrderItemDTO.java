package com.example.KitShop.DTOs;

import java.math.BigDecimal;

public record FullOrderItemDTO(
        String name,
        Long id,
        String imageUrl,
        BigDecimal price,
        String size,
        int quantity
) {}
