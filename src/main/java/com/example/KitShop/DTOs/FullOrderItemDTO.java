package com.example.KitShop.DTOs;

import java.math.BigDecimal;

public record FullOrderItemDTO(
        String name,
        String imageUrl,
        BigDecimal price,
        String size,
        int quantity
) {}
