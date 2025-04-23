package com.example.KitShop.ControllersTests;

import com.example.KitShop.Controllers.OrdersController;
import com.example.KitShop.DTOs.FullOrderDTO;
import com.example.KitShop.DTOs.FullOrderItemDTO;
import com.example.KitShop.DTOs.OrderItemDTO;
import com.example.KitShop.DTOs.OrdersDTO;
import com.example.KitShop.Services.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class OrdersControllerTest {

    private MockMvc mockMvc;

    @Mock private OrderServiceImpl ordersService;
    @InjectMocks private OrdersController ordersController;

    private OrdersDTO orderDTO;
    private OrderItemDTO orderItemDTO;
    private FullOrderDTO fullOrderDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ordersController).build();

        orderDTO = new OrdersDTO(1L, 1L, List.of(), "PENDING", "Test Address", BigDecimal.valueOf(100), LocalDateTime.now());
        orderItemDTO = new OrderItemDTO(1L, 2L, "M", 2);

        FullOrderItemDTO fullOrderItemDTO = new FullOrderItemDTO(
                "Cool T-Shirt",
                2L,
                "http://image.url/item.png",
                BigDecimal.valueOf(49.99),
                "M",
                2
        );

        fullOrderDTO = new FullOrderDTO(
                1L,
                "PENDING",
                "Test Address",
                1L,
                BigDecimal.valueOf(99.98),
                LocalDateTime.now(),
                List.of(fullOrderItemDTO)
        );
    }

    @Test
    void testBuyProduct_Success() throws Exception {
        when(ordersService.buyProduct(1L, 2L, "M", 1, "Test Address")).thenReturn(orderDTO);

        mockMvc.perform(post("/api/orders/buy")
                        .param("userId", "1")
                        .param("productId", "2")
                        .param("size", "M")
                        .param("shippingAddress", "Test Address")
                        .param("quantity", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOrderItemsByOrderId() throws Exception {
        when(ordersService.getOrderItemsByOrderId(1L)).thenReturn(List.of(orderItemDTO));

        mockMvc.perform(get("/api/orders/1/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testGetAllOrders() throws Exception {
        when(ordersService.getAllOrdersWithDetails()).thenReturn(List.of(fullOrderDTO));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testDeleteOrder_Success() throws Exception {
        doNothing().when(ordersService).deleteOrder(1L);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteOrder_NotFound() throws Exception {
        doThrow(new RuntimeException("Order not found")).when(ordersService).deleteOrder(1L);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProductId() throws Exception {
        when(ordersService.getProductIdByOrderItemId(1L)).thenReturn(2L);

        mockMvc.perform(get("/api/orders/item/1/product"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    void testCheckout_Success() throws Exception {
        when(ordersService.checkout(1L, "Test Address")).thenReturn(orderDTO);

        mockMvc.perform(post("/api/orders/checkout/1")
                        .param("shippingAddress", "Test Address"))
                .andExpect(status().isOk());
    }

    @Test
    void testCheckout_Failure() throws Exception {
        when(ordersService.checkout(1L, "Test Address")).thenThrow(new RuntimeException("Cart is empty"));

        mockMvc.perform(post("/api/orders/checkout/1")
                        .param("shippingAddress", "Test Address"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllUserOrders() throws Exception {
        when(ordersService.getAllOrdersWithDetailsByUserId(1L)).thenReturn(List.of(fullOrderDTO));

        mockMvc.perform(get("/api/orders/AllUserOrders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testCancelOrder_Success() throws Exception {
        doNothing().when(ordersService).cancelOrder(1L);

        mockMvc.perform(post("/api/orders/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order canceled successfully"));
    }

    @Test
    void testCancelOrder_NotFound() throws Exception {
        doThrow(new RuntimeException("Order not found")).when(ordersService).cancelOrder(1L);

        mockMvc.perform(post("/api/orders/1/cancel"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found"));
    }

    @Test
    void testApproveOrder() throws Exception {
        when(ordersService.approveOrder(1L)).thenReturn(orderDTO);

        mockMvc.perform(put("/api/orders/1/approve"))
                .andExpect(status().isOk());
    }
}
