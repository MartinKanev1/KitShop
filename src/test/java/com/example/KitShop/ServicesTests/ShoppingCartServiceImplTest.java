package com.example.KitShop.ServicesTests;

import com.example.KitShop.DTOs.CartItemDTO;
import com.example.KitShop.DTOs.ShoppingCartDTO;
import com.example.KitShop.Mappers.CartItemMapper;
import com.example.KitShop.Mappers.ShoppingCartMapper;
import com.example.KitShop.Models.*;
import com.example.KitShop.Repositories.*;
import com.example.KitShop.Services.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShoppingCartServiceImplTest {

    @Mock private ShoppingCartRepository shoppingCartRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProductKitsRepository productKitsRepository;
    @Mock private CartItemRepository cartItemRepository;
    @Mock private ShoppingCartMapper shoppingCartMapper;
    @Mock private CartItemMapper cartItemMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private User user;
    private ProductKits product;
    private ShoppingCart shoppingCart;
    private CartItem cartItem;
    private ShoppingCartDTO shoppingCartDTO;
    private CartItemDTO cartItemDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);

        product = new ProductKits();
        product.setProductKitId(100L);

        shoppingCart = new ShoppingCart();
        shoppingCart.setShoppingCartId(10L);
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(new ArrayList<>());

        cartItem = new CartItem();
        cartItem.setCartItemId(200L);
        cartItem.setProduct(product);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(1);

        List<CartItemDTO> cartItemsList = new ArrayList<>();

        shoppingCartDTO = new ShoppingCartDTO(10L, 1l,cartItemsList); // Add fields if needed
         // Add fields if needed
    }

    @Test
    void testAddProductToCart_CreatesNewCart() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productKitsRepository.findById(100L)).thenReturn(Optional.of(product));
        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(shoppingCartMapper.toDTO(any(ShoppingCart.class))).thenReturn(shoppingCartDTO);

        ShoppingCartDTO result = shoppingCartService.addProductToCart(1L, 100L,"M",14);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(productKitsRepository).findById(100L);
        verify(shoppingCartRepository).save(any(ShoppingCart.class));
        verify(cartItemRepository).save(any(CartItem.class));
        verify(shoppingCartMapper).toDTO(any(ShoppingCart.class));
    }

    @Test
    void testRemoveProductFromCart_Success() {
        shoppingCart.getCartItems().add(cartItem);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.findById(200L)).thenReturn(Optional.of(cartItem));
        when(shoppingCartMapper.toDTO(shoppingCart)).thenReturn(shoppingCartDTO);

        ShoppingCartDTO result = shoppingCartService.removeProductFromCart(1L, 200L);

        assertNotNull(result);
        verify(cartItemRepository).delete(cartItem);
        verify(shoppingCartMapper).toDTO(shoppingCart);
    }

    @Test
    void testGetCartItems_Success() {
        shoppingCart.setCartItems(List.of(cartItem));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(cartItemMapper.toDTO(cartItem)).thenReturn(cartItemDTO);

        List<CartItemDTO> items = shoppingCartService.getCartItems(1L);

        assertEquals(1, items.size());
        verify(cartItemMapper).toDTO(cartItem);
    }

    @Test
    void testGetProductIdByCartItemId_Success() {
        when(cartItemRepository.findById(200L)).thenReturn(Optional.of(cartItem));

        Long productId = shoppingCartService.getProductIdByCartItemId(200L);

        assertEquals(100L, productId);
    }

    @Test
    void testDeleteShoppingCart_Success() {
        user.setShoppingCartItems(shoppingCart);
        shoppingCart.setUser(user);

        when(shoppingCartRepository.findById(10L)).thenReturn(Optional.of(shoppingCart));

        shoppingCartService.deleteShoppingCart(10L);

        verify(userRepository).save(user);
        verify(cartItemRepository).deleteByShoppingCart_ShoppingCartId(10L);
        verify(shoppingCartRepository).deleteById(10L);
    }

    @Test
    void testGetShoppingCartIdByUserId_Success() {
        user.setShoppingCartItems(shoppingCart);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Long cartId = shoppingCartService.getShoppingCartIdByUserId(1L);

        assertEquals(10L, cartId);
    }
}

