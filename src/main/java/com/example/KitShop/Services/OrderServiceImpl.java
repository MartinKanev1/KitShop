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

    

    @Transactional
    public OrdersDTO buyProduct(Long userId, Long productId, String size, int quantity, String shippingAddress) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        ProductKits product = productKitsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));


        String requestedSize = size.trim().toUpperCase();

        ProductKitSizeQuantities matchingSize = product.getSizes().stream()
                .filter(s -> s.getSize() != null && s.getSize().trim().equalsIgnoreCase(requestedSize))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Size '" + size + "' not found for product ID: " + productId));


        if (matchingSize.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available for size " + size);
        }

        System.out.println("Available sizes: " + product.getSizes());


        Orders order = new Orders();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setAddress(shippingAddress);
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderItems(new ArrayList<>());


        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setSize(requestedSize);
        orderItem.setQuantity(quantity);
        orderItem.setPriceAtTimeOfPurchase(product.getPrice().doubleValue());

        order.getOrderItems().add(orderItem);


        ordersRepository.save(order);


        matchingSize.setQuantity(matchingSize.getQuantity() - quantity);
        productKitSizeQuantitiesRepository.save(matchingSize);

        return ordersMapper.toDTO(order);
    }

    @Transactional
    public OrdersDTO checkout(Long userId, String shippingAddress) {

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
            String size = cartItem.getSize();
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


            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setSize(normalizedSize);
            orderItem.setPriceAtTimeOfPurchase(product.getPrice().doubleValue());

            order.getOrderItems().add(orderItem);


            matchingSize.setQuantity(matchingSize.getQuantity() - quantity);
            productKitSizeQuantitiesRepository.save(matchingSize);

            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        order.setTotalAmount(totalAmount);
        ordersRepository.save(order);


        Long tmpId = shoppingCartServiceImpl.getShoppingCartIdByUserId(userId);
        shoppingCartServiceImpl.deleteShoppingCart(tmpId);

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
                        product.productKitId(),
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
                    order.userId(),
                    order.totalAmount(),
                    order.createdAt(),
                    itemDTOs
            );
        }).toList();
    }

    @Transactional
    public List<FullOrderDTO> getAllOrdersWithDetails() {
        List<Orders> orders = ordersRepository.findAll();

        return orders.stream().map(ordersMapper::toDTO).map(order -> {
            List<FullOrderItemDTO> itemDTOs = order.orderItems().stream().map(orderItem -> {
                Long productId = getProductIdByOrderItemId(orderItem.orderItemId());
                ProductKitsDTO product = productKitServiceImpl.getProductById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found for ID: " + productId));
                return new FullOrderItemDTO(
                        product.name(),
                        product.productKitId(),
                        product.imageUrl(),
                        product.price(),
                        orderItem.size(),
                        orderItem.quantity()
                );
            }).toList();

            Long userIdTo = ordersRepository.findUserIdByOrderId(order.orderId());

            return new FullOrderDTO(
                    order.orderId(),
                    order.status(),
                    order.address(),
                    userIdTo,
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


            sizeQuantity.setQuantity(sizeQuantity.getQuantity() + quantity);
            productKitSizeQuantitiesRepository.save(sizeQuantity);


            orderItemRepository.deleteById(orderItem.orderItemId());
        }


        ordersRepository.delete(order);

    }

    public OrdersDTO approveOrder(Long orderId) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException( "order not found"));

        order.setStatus("Approved");

        Orders approvedOrder = ordersRepository.save(order);

        return ordersMapper.toDTO(approvedOrder);

    }





}
