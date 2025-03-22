package com.example.KitShop.Services;

import com.example.KitShop.DTOs.OrderItemDTO;
import com.example.KitShop.DTOs.OrdersDTO;
import com.example.KitShop.Mappers.OrderItemMapper;
import com.example.KitShop.Mappers.OrdersMapper;
import com.example.KitShop.Models.*;
import com.example.KitShop.Repositories.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;
    private final OrdersMapper ordersMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductKitsRepository productKitsRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartServiceImpl shoppingCartServiceImpl;

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrdersRepository ordersRepository, OrdersMapper ordersMapper,UserRepository userRepository,ProductKitsRepository productKitsRepository,OrderItemRepository orderItemRepository,OrderItemMapper orderItemMapper,CartItemRepository cartItemRepository,ShoppingCartRepository shoppingCartRepository,ShoppingCartServiceImpl shoppingCartServiceImpl) {
        this.ordersRepository=ordersRepository;
        this.ordersMapper = ordersMapper;
        this.userRepository=userRepository;
        this.productKitsRepository=productKitsRepository;
        this.orderItemRepository=orderItemRepository;
        this.orderItemMapper=orderItemMapper;
        this.cartItemRepository=cartItemRepository;
        this.shoppingCartRepository=shoppingCartRepository;
        this.shoppingCartServiceImpl =shoppingCartServiceImpl;
    }

    

    public OrdersDTO buyProduct(Long userId, Long productId, int quantity, String shippingAddress) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        ProductKits product = productKitsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        // Create Order
        Orders order = new Orders();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setAddress(shippingAddress);
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        order.setCreatedAt(LocalDateTime.now());

        // Ensure orderItems list is initialized
        if (order.getOrderItems() == null) {
            order.setOrderItems(new ArrayList<>());
        }

        // Create OrderItem
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPriceAtTimeOfPurchase(product.getPrice().doubleValue()); // Store price history

        order.getOrderItems().add(orderItem); // ✅ Correctly associate OrderItem with Order

        // Save Order (CascadeType.ALL will persist order items)
        order = ordersRepository.save(order);

        // Save OrderItem separately if needed (not necessary with CascadeType.ALL)
        orderItemRepository.save(orderItem);

        // Update product stock
        product.setQuantity(product.getQuantity() - quantity);
        productKitsRepository.save(product);

        return ordersMapper.toDTO(order);
    }

    @Transactional
    public OrdersDTO checkout(Long userId, String shippingAddress) {
        // Fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));



        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found for user ID: " + userId));

        // Fetch shopping cart items
        List<CartItem> cartItems = cartItemRepository.findByShoppingCart(shoppingCart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Shopping cart is empty");
        }

        // Create Order
        Orders order = new Orders();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setAddress(shippingAddress);
        order.setOrderItems(new ArrayList<>()); // Initialize order items list

        BigDecimal totalAmount = BigDecimal.ZERO; // Track total order cost

        // Process each CartItem
        for (CartItem cartItem : cartItems) {
            ProductKits product = cartItem.getProduct();

            // Check if enough stock is available
            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock available for product: " + product.getName());
            }

            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtTimeOfPurchase(product.getPrice().doubleValue());

            order.getOrderItems().add(orderItem);

            // Deduct stock
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productKitsRepository.save(product);

            // Calculate total order amount
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        // Set total amount for the order
        order.setTotalAmount(totalAmount);

        // Save order (CascadeType.ALL will persist order items)
        order = ordersRepository.save(order);

        // Clear shopping cart


        Long tmpId = shoppingCartServiceImpl.getShoppingCartIdByUserId(userId);

        shoppingCartServiceImpl.deleteShoppingCart(tmpId);

        //shoppingCartServiceImpl.deleteShoppingCart(shoppingCart.getShoppingCartId());

        return ordersMapper.toDTO(order);
    }


    public List<OrderItemDTO> getOrderItemsByOrderId(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        return order.getOrderItems().stream()
                .map(orderItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<OrdersDTO> getAllOrders() {
        return ordersRepository.findAll().stream().map(ordersMapper::toDTO).collect(Collectors.toList());
    }

    public void deleteOrder(Long orderId) {

        ProductKits currentProduct = productKitsRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        productKitsRepository.delete(currentProduct);
    }

    public Long getProductIdByOrderItemId(Long cartItemId) {
        OrderItem orderItem = orderItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Order item not found with ID: " + cartItemId));

        if (orderItem.getProduct() == null) {
            throw new RuntimeException("Product not found for order item ID: " + cartItemId);
        }

        return orderItem.getProduct().getProductKitId();
    }


}
