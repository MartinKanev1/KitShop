package com.example.KitShop.Controllers;

import com.example.KitShop.DTOs.CartItemDTO;
import com.example.KitShop.DTOs.ShoppingCartDTO;
import com.example.KitShop.Services.ShoppingCartServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartServiceImpl shoppingCartServiceImpl;

    public ShoppingCartController(ShoppingCartServiceImpl shoppingCartServiceImpl) {
        this.shoppingCartServiceImpl = shoppingCartServiceImpl;
    }

    @PostMapping("/add")
    public ResponseEntity<ShoppingCartDTO> addProductToCart(@RequestParam Long userId, @RequestParam Long productId) {
        ShoppingCartDTO updatedCart = shoppingCartServiceImpl.addProductToCart(userId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping("/{userId}/items")
    public ResponseEntity<List<CartItemDTO>> getCartItems(@PathVariable Long userId) {
        List<CartItemDTO> cartItems = shoppingCartServiceImpl.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/item/{cartItemId}/product")
    public ResponseEntity<Long> getProductId(@PathVariable Long cartItemId) {
        Long productId = shoppingCartServiceImpl.getProductIdByCartItemId(cartItemId);
        return ResponseEntity.ok(productId);
    }

    @DeleteMapping("/{userId}/item/{cartItemId}")
    public ResponseEntity<ShoppingCartDTO> removeProductFromCart(@PathVariable Long userId, @PathVariable Long cartItemId) {
        ShoppingCartDTO updatedCart = shoppingCartServiceImpl.removeProductFromCart(userId, cartItemId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{shoppingCartId}")
    public ResponseEntity<String> deleteShoppingCart(@PathVariable Long shoppingCartId) {
        shoppingCartServiceImpl.deleteShoppingCart(shoppingCartId);
        return ResponseEntity.ok("Shopping cart with ID " + shoppingCartId + " has been deleted successfully.");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Long> getShoppingCartIdByUserId(@PathVariable Long userId) {
        Long shoppingCartId = shoppingCartServiceImpl.getShoppingCartIdByUserId(userId);
        return ResponseEntity.ok(shoppingCartId);
    }
}
