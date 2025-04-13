//package com.example.KitShop.ControllersTests;
//
//import com.example.KitShop.Controllers.OrdersController;
//import com.example.KitShop.DTOs.OrderItemDTO;
//import com.example.KitShop.DTOs.OrdersDTO;
//import com.example.KitShop.Services.OrderServiceImpl;
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
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//@Disabled
//public class OrdersControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock private OrderServiceImpl ordersService;
//
//    @InjectMocks private OrdersController ordersController;
//
//    private OrdersDTO orderDTO;
//    private OrderItemDTO orderItemDTO;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(ordersController).build();
//
//        orderDTO = new OrdersDTO(
//                500L, 1L, List.of(), "PENDING", "Test Address",
//                BigDecimal.valueOf(99.99), LocalDateTime.now()
//        );
//
//        orderItemDTO = new OrderItemDTO(600L, 500L, 2 );
//    }
//
//    @Test
//    void testBuyProduct_Success() throws Exception {
//        when(ordersService.buyProduct(1L, 100L, 2, "Test Address")).thenReturn(orderDTO);
//
//        mockMvc.perform(post("/api/orders/buy")
//                        .param("userId", "1")
//                        .param("productId", "100")
//                        .param("quantity", "2")
//                        .param("shippingAddress", "Test Address"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("PENDING"))
//                .andExpect(jsonPath("$.totalAmount").value(99.99));
//
//        verify(ordersService).buyProduct(1L, 100L, 2, "Test Address");
//    }
//
//    @Test
//    void testBuyProduct_Failure() throws Exception {
//        when(ordersService.buyProduct(1L, 100L, 2, "Test Address")).thenThrow(new RuntimeException("Error"));
//
//        mockMvc.perform(post("/api/orders/buy")
//                        .param("userId", "1")
//                        .param("productId", "100")
//                        .param("quantity", "2")
//                        .param("shippingAddress", "Test Address"))
//                .andExpect(status().isBadRequest());
//
//        verify(ordersService).buyProduct(1L, 100L, 2, "Test Address");
//    }
//
//    @Test
//    void testGetOrderItemsByOrderId_Success() throws Exception {
//        when(ordersService.getOrderItemsByOrderId(500L)).thenReturn(List.of(orderItemDTO));
//
//        mockMvc.perform(get("/api/orders/500/items"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(1))
//                .andExpect(jsonPath("$[0].orderItemId").value(600));
//
//        verify(ordersService).getOrderItemsByOrderId(500L);
//    }
//
//    @Test
//    void testGetAllOrders_Success() throws Exception {
//        when(ordersService.getAllOrders()).thenReturn(List.of(orderDTO));
//
//        mockMvc.perform(get("/api/orders"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(1))
//                .andExpect(jsonPath("$[0].status").value("PENDING"));
//
//        verify(ordersService).getAllOrders();
//    }
//
//    @Test
//    void testDeleteOrder_Success() throws Exception {
//        doNothing().when(ordersService).deleteOrder(500L);
//
//        mockMvc.perform(delete("/api/orders/500"))
//                .andExpect(status().isNoContent());
//
//        verify(ordersService).deleteOrder(500L);
//    }
//
//    @Test
//    void testDeleteOrder_NotFound() throws Exception {
//        doThrow(new RuntimeException("Order not found")).when(ordersService).deleteOrder(500L);
//
//        mockMvc.perform(delete("/api/orders/500"))
//                .andExpect(status().isNotFound());
//
//        verify(ordersService).deleteOrder(500L);
//    }
//
//    @Test
//    void testGetProductIdByOrderItemId_Success() throws Exception {
//        when(ordersService.getProductIdByOrderItemId(600L)).thenReturn(100L);
//
//        mockMvc.perform(get("/api/orders/item/600/product"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("100"));
//
//        verify(ordersService).getProductIdByOrderItemId(600L);
//    }
//
//    @Test
//    void testCheckout_Success() throws Exception {
//        when(ordersService.checkout(1L, "Test Address")).thenReturn(orderDTO);
//
//        mockMvc.perform(post("/api/orders/checkout/1")
//                        .param("shippingAddress", "Test Address"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("PENDING"))
//                .andExpect(jsonPath("$.totalAmount").value(99.99));
//
//        verify(ordersService).checkout(1L, "Test Address");
//    }
//
//    @Test
//    void testCheckout_Failure() throws Exception {
//        when(ordersService.checkout(1L, "Test Address")).thenThrow(new RuntimeException("Cart is empty"));
//
//        mockMvc.perform(post("/api/orders/checkout/1")
//                        .param("shippingAddress", "Test Address"))
//                .andExpect(status().isBadRequest());
//
//        verify(ordersService).checkout(1L, "Test Address");
//    }
//}
