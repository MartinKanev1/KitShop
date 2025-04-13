//package com.example.KitShop.ControllersTests;
//
//import com.example.KitShop.Controllers.ShoppingCartController;
//import com.example.KitShop.DTOs.CartItemDTO;
//import com.example.KitShop.DTOs.ShoppingCartDTO;
//import com.example.KitShop.Services.ShoppingCartServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//@Disabled
//public class ShoppingCartControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock private ShoppingCartServiceImpl shoppingCartServiceImpl;
//
//    @InjectMocks private ShoppingCartController shoppingCartController;
//
//    private ShoppingCartDTO shoppingCartDTO;
//    private CartItemDTO cartItemDTO;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(shoppingCartController).build();
//
//        shoppingCartDTO = new ShoppingCartDTO(10L, 1L, List.of());
//        cartItemDTO = new CartItemDTO(200L, 100L,  2);
//    }
//
//    @Test
//    void testAddProductToCart_Success() throws Exception {
//        when(shoppingCartServiceImpl.addProductToCart(1L, 100L)).thenReturn(shoppingCartDTO);
//
//        mockMvc.perform(post("/api/shopping-cart/add")
//                        .param("userId", "1")
//                        .param("productId", "100"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.shoppingCartId").value(10))
//                .andExpect(jsonPath("$.userId").value(1));
//
//        verify(shoppingCartServiceImpl).addProductToCart(1L, 100L);
//    }
//
//    @Test
//    void testGetCartItems_Success() throws Exception {
//        when(shoppingCartServiceImpl.getCartItems(1L)).thenReturn(List.of(cartItemDTO));
//
//        mockMvc.perform(get("/api/shopping-cart/1/items"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(1))
//                .andExpect(jsonPath("$[0].cartItemId").value(200));
//
//        verify(shoppingCartServiceImpl).getCartItems(1L);
//    }
//
//    @Test
//    void testGetProductId_Success() throws Exception {
//        when(shoppingCartServiceImpl.getProductIdByCartItemId(200L)).thenReturn(100L);
//
//        mockMvc.perform(get("/api/shopping-cart/item/200/product"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("100"));
//
//        verify(shoppingCartServiceImpl).getProductIdByCartItemId(200L);
//    }
//
//    @Test
//    void testRemoveProductFromCart_Success() throws Exception {
//        when(shoppingCartServiceImpl.removeProductFromCart(1L, 200L)).thenReturn(shoppingCartDTO);
//
//        mockMvc.perform(delete("/api/shopping-cart/1/item/200"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.shoppingCartId").value(10));
//
//        verify(shoppingCartServiceImpl).removeProductFromCart(1L, 200L);
//    }
//
//    @Test
//    void testDeleteShoppingCart_Success() throws Exception {
//        doNothing().when(shoppingCartServiceImpl).deleteShoppingCart(10L);
//
//        mockMvc.perform(delete("/api/shopping-cart/10"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Shopping cart with ID 10 has been deleted successfully."));
//
//        verify(shoppingCartServiceImpl).deleteShoppingCart(10L);
//    }
//
//    @Test
//    void testGetShoppingCartIdByUserId_Success() throws Exception {
//        when(shoppingCartServiceImpl.getShoppingCartIdByUserId(1L)).thenReturn(10L);
//
//        mockMvc.perform(get("/api/shopping-cart/user/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("10"));
//
//        verify(shoppingCartServiceImpl).getShoppingCartIdByUserId(1L);
//    }
//}
