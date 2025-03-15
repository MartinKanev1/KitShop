package com.example.KitShop.Services;

import com.example.KitShop.DTOs.CartItemDTO;
import com.example.KitShop.DTOs.ShoppingCartDTO;
import com.example.KitShop.Mappers.CartItemMapper;
import com.example.KitShop.Mappers.ShoppingCartMapper;
import com.example.KitShop.Models.CartItem;
import com.example.KitShop.Models.ProductKits;
import com.example.KitShop.Models.ShoppingCart;
import com.example.KitShop.Models.User;
import com.example.KitShop.Repositories.CartItemRepository;
import com.example.KitShop.Repositories.ProductKitsRepository;
import com.example.KitShop.Repositories.ShoppingCartRepository;
import com.example.KitShop.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements  ShoppingCartService{

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ProductKitsRepository productKitsRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,ShoppingCartMapper shoppingCartMapper,UserRepository userRepository,ProductKitsRepository productKitsRepository,CartItemRepository cartItemRepository,CartItemMapper cartItemMapper) {
        this.shoppingCartRepository=shoppingCartRepository;
        this.shoppingCartMapper=shoppingCartMapper;
        this.userRepository=userRepository;
        this.productKitsRepository=productKitsRepository;
        this.cartItemRepository=cartItemRepository;
        this.cartItemMapper=cartItemMapper;
    }

    public ShoppingCartDTO addProductToCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        ProductKits product = productKitsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUser(user);
                    return shoppingCartRepository.save(newCart);
                });


            // Add a new cart item if the product is not in the cart
            CartItem cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItemRepository.save(cartItem);


        // 4. Save the changes
        shoppingCartRepository.save(shoppingCart);

        // 5. Return the updated shopping cart DTO
        return shoppingCartMapper.toDTO(shoppingCart);



    }


    public ShoppingCartDTO removeProductFromCart(Long userId, Long cartItemId) {
        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Find shopping cart
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found for user ID: " + userId));

        // Find the cart item
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with ID: " + cartItemId));

        // Ensure the cart item belongs to the user's shopping cart
        if (!cartItem.getShoppingCart().equals(shoppingCart)) {
            throw new RuntimeException("Cart item does not belong to the user's shopping cart");
        }

        // Remove the cart item
        cartItemRepository.delete(cartItem);

        // Save the updated shopping cart
        shoppingCartRepository.save(shoppingCart);

        // Return updated shopping cart DTO
        return shoppingCartMapper.toDTO(shoppingCart);
    }


    public List<CartItemDTO> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found for user ID: " + userId));

        return shoppingCart.getCartItems().stream()
                .map(cartItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Long getProductIdByCartItemId(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with ID: " + cartItemId));

        if (cartItem.getProduct() == null) {
            throw new RuntimeException("Product not found for cart item ID: " + cartItemId);
        }

        return cartItem.getProduct().getProductKitId();
    }

//    @Transactional
//    public void deleteShoppingCart (Long shoppingCartId) {
//        ShoppingCart cart = shoppingCartRepository.findById(shoppingCartId)
//                .orElseThrow(() -> new IllegalArgumentException("Shopping Cart with ID " + shoppingCartId + " not found"));
//
//        if (!shoppingCartRepository.existsById(shoppingCartId)) {
//            throw new RuntimeException("Shopping Cart with ID " + shoppingCartId + " does not exist.");
//        }
//
//        cartItemRepository.deleteByShoppingCart_ShoppingCartId(shoppingCartId);
//
//        shoppingCartRepository.deleteById(shoppingCartId);
//
//
//    }

    @Transactional
    public void deleteShoppingCart(Long shoppingCartId) {
        Optional<ShoppingCart> shoppingCartOpt = shoppingCartRepository.findById(shoppingCartId);

        if (shoppingCartOpt.isPresent()) {
            ShoppingCart shoppingCart = shoppingCartOpt.get();

            // Step 1: Detach ShoppingCart from User
            User user = shoppingCart.getUser();
            if (user != null) {
                user.setShoppingCartItems(null); // Remove reference to ShoppingCart
                userRepository.save(user); // Save user to persist the change
                //log.info("Detached ShoppingCart from User.");
            }

            // Step 2: Delete all cart items first
            cartItemRepository.deleteByShoppingCart_ShoppingCartId(shoppingCartId);

            // Step 3: Delete the ShoppingCart
            shoppingCartRepository.deleteById(shoppingCartId);
            //log.info("ShoppingCart deleted successfully.");
        } else {
            //log.warn("ShoppingCart with ID " + shoppingCartId + " does not exist.");
        }
    }


    public Long getShoppingCartIdByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        return user.getShoppingCartItems().getShoppingCartId();
    }

}
