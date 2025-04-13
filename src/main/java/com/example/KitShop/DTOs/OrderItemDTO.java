package com.example.KitShop.DTOs;

public record OrderItemDTO(
        Long orderItemId,
        Long productId,
        String size,
        int quantity

) {
}
