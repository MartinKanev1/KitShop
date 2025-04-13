package com.example.KitShop.DTOs;

public record CartItemDTO(

        Long cartItemId,
        Long productId,
        String size,
        int quantity


) {
}
