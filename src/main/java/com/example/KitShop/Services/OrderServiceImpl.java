package com.example.KitShop.Services;

import com.example.KitShop.DTOs.*;
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
    private final ProductKitSizeQuantitiesRepository productKitSizeQuantitiesRepository;
    private final ProductKitServiceImpl productKitServiceImpl;

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrdersRepository ordersRepository, OrdersMapper ordersMapper,UserRepository userRepository,ProductKitsRepository productKitsRepository,OrderItemRepository orderItemRepository,OrderItemMapper orderItemMapper,CartItemRepository cartItemRepository,ShoppingCartRepository shoppingCartRepository,ShoppingCartServiceImpl shoppingCartServiceImpl,ProductKitSizeQuantitiesRepository productKitSizeQuantitiesRepository,ProductKitServiceImpl productKitServiceImpl) {
        this.ordersRepository=ordersRepository;
        this.ordersMapper = ordersMapper;
        this.userRepository=userRepository;
        this.productKitsRepository=productKitsRepository;
        this.orderItemRepository=orderItemRepository;
        this.orderItemMapper=orderItemMapper;
        this.cartItemRepository=cartItemRepository;
        this.shoppingCartRepository=shoppingCartRepository;
        this.shoppingCartServiceImpl =shoppingCartServiceImpl;
        this.productKitSizeQuantitiesRepository = productKitSizeQuantitiesRepository;
        this.productKitServiceImpl=productKitServiceImpl;
    }

    
    /*
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
   */

    //raboti sled finalni promeni!!
    @Transactional
    public OrdersDTO buyProduct(Long userId, Long productId, String size, int quantity, String shippingAddress) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        ProductKits product = productKitsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // 🧠 Normalize and match the size string
        String requestedSize = size.trim().toUpperCase();

        ProductKitSizeQuantities matchingSize = product.getSizes().stream()
                .filter(s -> s.getSize() != null && s.getSize().trim().equalsIgnoreCase(requestedSize))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Size '" + size + "' not found for product ID: " + productId));

        // ❗ Check available stock
        if (matchingSize.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available for size " + size);
        }

        System.out.println("Available sizes: " + product.getSizes());

        // 🧾 Create the order
        Orders order = new Orders();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setAddress(shippingAddress);
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderItems(new ArrayList<>());

        // 📦 Create order item
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setSize(requestedSize); // <- you might want to save size in OrderItem!
        orderItem.setQuantity(quantity);
        orderItem.setPriceAtTimeOfPurchase(product.getPrice().doubleValue());

        order.getOrderItems().add(orderItem);

        // 🧾 Save everything
        ordersRepository.save(order);

        // 🔄 Update product size quantity
        matchingSize.setQuantity(matchingSize.getQuantity() - quantity);
        productKitSizeQuantitiesRepository.save(matchingSize); // <- don't forget to wire this repo!

        return ordersMapper.toDTO(order);
    }

    @Transactional
    public OrdersDTO checkout(Long userId, String shippingAddress) {
        // 🔍 Fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found for user ID: " + userId));

        List<CartItem> cartItems = cartItemRepository.findByShoppingCart(shoppingCart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Shopping cart is empty");
        }

        Orders order = new Orders();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setAddress(shippingAddress);
        order.setOrderItems(new ArrayList<>());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            ProductKits product = cartItem.getProduct();
            String size = cartItem.getSize(); // 🔥 Assuming this is stored in your CartItem entity
            int quantity = cartItem.getQuantity();

            // Normalize size
            String normalizedSize = size.trim().toUpperCase();

            ProductKitSizeQuantities matchingSize = product.getSizes().stream()
                    .filter(s -> s.getSize() != null && s.getSize().trim().equalsIgnoreCase(normalizedSize))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Size '" + size + "' not found for product ID: " + product.getProductKitId()));

            if (matchingSize.getQuantity() < quantity) {
                throw new RuntimeException("Not enough stock for size '" + size + "' in product: " + product.getName());
            }

            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setSize(normalizedSize); // ✅ Save size!
            orderItem.setPriceAtTimeOfPurchase(product.getPrice().doubleValue());

            order.getOrderItems().add(orderItem);

            // Deduct stock from specific size
            matchingSize.setQuantity(matchingSize.getQuantity() - quantity);
            productKitSizeQuantitiesRepository.save(matchingSize);

            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        order.setTotalAmount(totalAmount);
        ordersRepository.save(order);

        // 🧹 Clear shopping cart
        Long tmpId = shoppingCartServiceImpl.getShoppingCartIdByUserId(userId);
        shoppingCartServiceImpl.deleteShoppingCart(tmpId);

        return ordersMapper.toDTO(order);
    }




    @Override
    public OrdersDTO buyProduct(Long userId, Long productId, int quantity, String size, String shippingAddress) {
        return null;
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


//    @Transactional
//    public List<FullOrderDTO> getAllOrdersWithDetailsByUserId(Long userId) {
//        List<OrdersDTO> orders = ordersRepository.findByUser_UserId(userId);
//
//        return orders.stream().map(order -> {
//            List<FullOrderItemDTO> itemDTOs = order.orderItems().stream().map(orderItem -> {
//                // Step 1: Get productId from orderItem
//                Long productId = getProductIdByOrderItemId(orderItem.orderItemId());
//
//                // Step 2: Fetch product details
//                ProductKitsDTO product = productKitServiceImpl.getProductById(productId).orElseThrow(() ->
//                        new RuntimeException("Product not found for ID: " + productId));
//
//                // Step 3: Build enriched item DTO
//                return new FullOrderItemDTO(
//                        product.name(),
//                        product.imageUrl(),
//                        product.price(),
//                        orderItem.size(),
//                        orderItem.quantity()
//                );
//            }).collect(Collectors.toList());
//
//            // Step 4: Build enriched order DTO
//            return new FullOrderDTO(
//                    order.orderId(),
//                    order.status(),
//                    order.address(),
//                    order.totalAmount(),
//                    order.createdAt(),
//                    itemDTOs
//            );
//        }).collect(Collectors.toList());
//    }

    @Transactional
    public List<FullOrderDTO> getAllOrdersWithDetailsByUserId(Long userId) {
        List<Orders> orders = ordersRepository.findByUser_UserId(userId);

        return orders.stream().map(ordersMapper::toDTO).map(order -> {
            List<FullOrderItemDTO> itemDTOs = order.orderItems().stream().map(orderItem -> {
                Long productId = getProductIdByOrderItemId(orderItem.orderItemId());
                ProductKitsDTO product = productKitServiceImpl.getProductById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found for ID: " + productId));
                return new FullOrderItemDTO(
                        product.name(),
                        product.imageUrl(),
                        product.price(),
                        orderItem.size(),
                        orderItem.quantity()
                );
            }).toList();

            return new FullOrderDTO(
                    order.orderId(),
                    order.status(),
                    order.address(),
                    order.totalAmount(),
                    order.createdAt(),
                    itemDTOs
            );
        }).toList();
    }


    @Transactional
    public void cancelOrder(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found for ID: " + orderId));

        List<OrderItemDTO> orderItems = getOrderItemsByOrderId(orderId);

        for (OrderItemDTO orderItem : orderItems) {
            Long productId = getProductIdByOrderItemId(orderItem.orderItemId());
            String size = orderItem.size();
            int quantity = orderItem.quantity();

            // Find the specific size quantity row
            ProductKitSizeQuantities sizeQuantity = (ProductKitSizeQuantities) productKitSizeQuantitiesRepository
                    .findByProductKit_ProductKitIdAndSize(productId, size)
                    .orElseThrow(() -> new RuntimeException(
                            "Size quantity not found for productId " + productId + " and size " + size
                    ));

            // Add the quantity back
            sizeQuantity.setQuantity(sizeQuantity.getQuantity() + quantity);
            productKitSizeQuantitiesRepository.save(sizeQuantity);

            // Delete the order item
            orderItemRepository.deleteById(orderItem.orderItemId());
        }

        // Delete the order
        ordersRepository.delete(order);

    }





}
