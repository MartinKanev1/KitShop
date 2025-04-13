package com.example.KitShop.Controllers;

import com.example.KitShop.DTOs.FullOrderDTO;
import com.example.KitShop.DTOs.OrderItemDTO;
import com.example.KitShop.DTOs.OrdersDTO;
import com.example.KitShop.Services.OrderServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    private final OrderServiceImpl ordersService;

    public OrdersController(OrderServiceImpl ordersService) {
        this.ordersService = ordersService;
    }


    @PostMapping("/buy")
    public ResponseEntity<OrdersDTO> buyProduct(@RequestParam Long userId,@RequestParam  Long productId,@RequestParam String size ,@RequestParam String shippingAddress, @RequestParam int quantity) {
        try {
            OrdersDTO order = ordersService.buyProduct(userId, productId, size  ,quantity,shippingAddress);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            System.out.println("🛑 Error while processing buy request: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItemDTO>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItemDTO> orderItems = ordersService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }

    @GetMapping
    public ResponseEntity<List<OrdersDTO>> getAllOffers() {
        List<OrdersDTO> offers = ordersService.getAllOrders();
        return ResponseEntity.ok(offers);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            ordersService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/item/{orderItemId}/product")
    public ResponseEntity<Long> getProductId(@PathVariable Long orderItemId) {
        Long productId = ordersService.getProductIdByOrderItemId(orderItemId);
        return ResponseEntity.ok(productId);
    }


    @PostMapping("/checkout/{userId}")
    public ResponseEntity<OrdersDTO> checkout(@PathVariable Long userId, @RequestParam String shippingAddress) {
        try {
            OrdersDTO orderDTO = ordersService.checkout(userId, shippingAddress);
            return ResponseEntity.ok(orderDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/AllUserOrders/{userId}")
    public ResponseEntity<List<FullOrderDTO>> getMyOrdersForTest(@PathVariable Long userId) {

        List<FullOrderDTO> orders = ordersService.getAllOrdersWithDetailsByUserId(userId);
        return ResponseEntity.ok(orders);


    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        try {
            ordersService.cancelOrder(orderId);
            return ResponseEntity.ok("Order canceled successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to cancel order");
        }
    }



}
