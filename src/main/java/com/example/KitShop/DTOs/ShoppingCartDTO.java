package com.example.KitShop.DTOs;

import java.util.List;

public record ShoppingCartDTO(

        Long shoppingCartId,
        Long userId,
        List<CartItemDTO> cartItems
) {
}
