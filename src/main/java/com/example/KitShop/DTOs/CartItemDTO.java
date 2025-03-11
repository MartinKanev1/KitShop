package com.example.KitShop.DTOs;

public record CartItemDTO(

        Long cartItemId,
        Long productId,
        int quantity


) {
}
