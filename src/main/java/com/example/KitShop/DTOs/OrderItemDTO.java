package com.example.KitShop.DTOs;

public record OrderItemDTO(
        Long orderItemId,
        Long productId,
        int quantity

) {
}
