package com.example.KitShop.Services;

import com.example.KitShop.DTOs.CartItemDTO;
import com.example.KitShop.DTOs.ShoppingCartDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShoppingCartService {

    ShoppingCartDTO addProductToCart(Long userId, Long productId, String size, int quantity);

    ShoppingCartDTO removeProductFromCart(Long userId, Long cartItemId);

    void deleteShoppingCart(Long shoppingCartId);

    List<CartItemDTO> getCartItems(Long userId);

    Long getProductIdByCartItemId(Long cartItemId);

    Long getShoppingCartIdByUserId(Long userId);
}
