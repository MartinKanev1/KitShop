//package com.example.KitShop.ServicesTests;
//
//import com.example.KitShop.DTOs.OrderItemDTO;
//import com.example.KitShop.DTOs.OrdersDTO;
//import com.example.KitShop.Mappers.OrderItemMapper;
//import com.example.KitShop.Mappers.OrdersMapper;
//import com.example.KitShop.Models.*;
//import com.example.KitShop.Repositories.*;
//import com.example.KitShop.Services.OrderServiceImpl;
//import com.example.KitShop.Services.ShoppingCartServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//@Disabled
//public class OrderServiceImplTest {
//
//    @Mock private OrdersRepository ordersRepository;
//    @Mock private UserRepository userRepository;
//    @Mock private ProductKitsRepository productKitsRepository;
//    @Mock private OrderItemRepository orderItemRepository;
//    @Mock private CartItemRepository cartItemRepository;
//    @Mock private ShoppingCartRepository shoppingCartRepository;
//    @Mock private OrdersMapper ordersMapper;
//    @Mock private OrderItemMapper orderItemMapper;
//    @Mock private ShoppingCartServiceImpl shoppingCartServiceImpl;
//
//    @InjectMocks private OrderServiceImpl orderService;
//
//    private User user;
//    private ProductKits product;
//    private Orders order;
//    private OrderItem orderItem;
//    private OrdersDTO orderDTO;
//    private OrderItemDTO orderItemDTO;
//    private ShoppingCart shoppingCart;
//    private CartItem cartItem;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        user = new User();
//        user.setUserId(1L);
//
//        product = new ProductKits();
//        product.setProductKitId(100L);
//        product.setName("Football Kit");
//        product.setPrice(BigDecimal.valueOf(49.99));
//        product.setQuantity(10);
//
//        order = new Orders();
//        order.setOrderId(500L);
//        order.setUser(user);
//        order.setStatus("PENDING");
//        order.setTotalAmount(BigDecimal.valueOf(99.98));
//        order.setCreatedAt(LocalDateTime.now());
//        order.setOrderItems(new ArrayList<>());
//
//        orderItem = new OrderItem();
//        orderItem.setOrder(order);
//        orderItem.setProduct(product);
//        orderItem.setQuantity(2);
//        orderItem.setPriceAtTimeOfPurchase(49.99);
//
//        order.getOrderItems().add(orderItem);
//
//        List<OrderItemDTO> orderItemsList = (orderItemDTO != null) ? List.of(orderItemDTO) : new ArrayList<>();
//        orderDTO = new OrdersDTO(
//                500L,
//                1L,
//                orderItemsList,  // ✅ Now it won’t break even if `orderItemDTO` is null
//                "PENDING",
//                "Test Address",
//                BigDecimal.valueOf(99.98),
//                LocalDateTime.now()
//        );
//
//
//
//        orderItemDTO = new OrderItemDTO(600L, 500L,  2);
//
//        shoppingCart = new ShoppingCart();
//        shoppingCart.setShoppingCartId(300L);
//        shoppingCart.setUser(user);
//
//        cartItem = new CartItem();
//        cartItem.setShoppingCart(shoppingCart);
//        cartItem.setProduct(product);
//        cartItem.setQuantity(2);
//    }
//
//    @Test
//    void testBuyProduct_Success() {
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(productKitsRepository.findById(100L)).thenReturn(Optional.of(product));
//        when(ordersRepository.save(any(Orders.class))).thenReturn(order);
//        when(ordersMapper.toDTO(order)).thenReturn(orderDTO);
//
//        OrdersDTO result = orderService.buyProduct(1L, 100L, 2, "Test Address");
//
//        assertNotNull(result);
//        assertEquals("PENDING", result.status());
//        verify(productKitsRepository).save(product);
//        verify(ordersRepository).save(any(Orders.class));
//    }
//
//    @Test
//    void testCheckout_Success() {
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
//        when(cartItemRepository.findByShoppingCart(shoppingCart)).thenReturn(List.of(cartItem));
//        when(ordersRepository.save(any(Orders.class))).thenReturn(order);
//        when(ordersMapper.toDTO(order)).thenReturn(orderDTO);
//        when(shoppingCartServiceImpl.getShoppingCartIdByUserId(1L)).thenReturn(300L);
//        doNothing().when(shoppingCartServiceImpl).deleteShoppingCart(300L);
//
//        OrdersDTO result = orderService.checkout(1L, "Test Address");
//
//        assertNotNull(result);
//        assertEquals("PENDING", result.status());
//        verify(ordersRepository).save(any(Orders.class));
//        verify(shoppingCartServiceImpl).deleteShoppingCart(300L);
//    }
//
//    @Test
//    void testGetOrderItemsByOrderId_Success() {
//        when(ordersRepository.findById(500L)).thenReturn(Optional.of(order));
//        when(orderItemMapper.toDTO(orderItem)).thenReturn(orderItemDTO);
//
//        List<OrderItemDTO> results = orderService.getOrderItemsByOrderId(500L);
//
//        assertEquals(1, results.size());
//        assertEquals(2, results.get(0).quantity());
//        verify(orderItemMapper).toDTO(orderItem);
//    }
//
//    @Test
//    void testGetAllOrders_Success() {
//        when(ordersRepository.findAll()).thenReturn(List.of(order));
//        when(ordersMapper.toDTO(order)).thenReturn(orderDTO);
//
//        List<OrdersDTO> results = orderService.getAllOrders();
//
//        assertEquals(1, results.size());
//        assertEquals("PENDING", results.get(0).status());
//        verify(ordersRepository).findAll();
//        verify(ordersMapper).toDTO(order);
//    }
//
//    @Test
//    void testDeleteOrder_Success() {
//        when(productKitsRepository.findById(500L)).thenReturn(Optional.of(product));
//
//        orderService.deleteOrder(500L);
//
//        verify(productKitsRepository).delete(product);
//    }
//
//    @Test
//    void testGetProductIdByOrderItemId_Success() {
//        when(orderItemRepository.findById(600L)).thenReturn(Optional.of(orderItem));
//
//        Long productId = orderService.getProductIdByOrderItemId(600L);
//
//        assertEquals(100L, productId);
//    }
//}
