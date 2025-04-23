package com.example.KitShop.Services;

import com.example.KitShop.DTOs.CartItemDTO;
import com.example.KitShop.DTOs.ShoppingCartDTO;
import com.example.KitShop.Mappers.CartItemMapper;
import com.example.KitShop.Mappers.ShoppingCartMapper;
import com.example.KitShop.Models.*;
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






    public ShoppingCartDTO addProductToCart(Long userId, Long productId, String size, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        ProductKits product = productKitsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        String normalizedSize = size.trim().toUpperCase();


        ProductKitSizeQuantities sizeEntry = product.getSizes().stream()
                .filter(s -> s.getSize() != null && s.getSize().trim().equalsIgnoreCase(normalizedSize))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Size '" + size + "' not available for this product."));

        int availableStock = sizeEntry.getQuantity();


        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUser(user);
                    return shoppingCartRepository.save(newCart);
                });


        Optional<CartItem> existingCartItem = cartItemRepository
                .findByShoppingCartAndProductAndSize(shoppingCart, product, normalizedSize);

        int currentQuantityInCart = existingCartItem.map(CartItem::getQuantity).orElse(0);
        int newTotalRequested = currentQuantityInCart + quantity;

        if (newTotalRequested > availableStock) {
            throw new RuntimeException("Only " + availableStock + " items available in size '" + normalizedSize + "'. You already have " + currentQuantityInCart + " in your cart.");
        }

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(newTotalRequested);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setShoppingCart(shoppingCart);
            newCartItem.setProduct(product);
            newCartItem.setSize(normalizedSize);
            newCartItem.setQuantity(quantity);
            cartItemRepository.save(newCartItem);
        }

        return shoppingCartMapper.toDTO(shoppingCart);
    }




    public ShoppingCartDTO removeProductFromCart(Long userId, Long cartItemId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));


        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found for user ID: " + userId));


        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with ID: " + cartItemId));


        if (!cartItem.getShoppingCart().equals(shoppingCart)) {
            throw new RuntimeException("Cart item does not belong to the user's shopping cart");
        }


        cartItemRepository.delete(cartItem);


        shoppingCartRepository.save(shoppingCart);


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



    @Transactional
    public void deleteShoppingCart(Long shoppingCartId) {
        Optional<ShoppingCart> shoppingCartOpt = shoppingCartRepository.findById(shoppingCartId);

        if (shoppingCartOpt.isPresent()) {
            ShoppingCart shoppingCart = shoppingCartOpt.get();


            User user = shoppingCart.getUser();
            if (user != null) {
                user.setShoppingCartItems(null);
                userRepository.save(user);

            }


            cartItemRepository.deleteByShoppingCart_ShoppingCartId(shoppingCartId);


            shoppingCartRepository.deleteById(shoppingCartId);

        } else {

        }
    }


    public Long getShoppingCartIdByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        return user.getShoppingCartItems().getShoppingCartId();
    }

}
