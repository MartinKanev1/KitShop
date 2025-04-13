//package com.example.KitShop.ServicesTests;
//
//
//
//import com.example.KitShop.DTOs.ProductKitsDTO;
//import com.example.KitShop.DTOs.ReviewsDTO;
//import com.example.KitShop.Mappers.ProductKitsMapper;
//import com.example.KitShop.Models.*;
//import com.example.KitShop.Repositories.ProductKitsRepository;
//import com.example.KitShop.Repositories.ReviewsRepository;
//import com.example.KitShop.Repositories.UserRepository;
//import com.example.KitShop.Services.CloudinaryService;
//import com.example.KitShop.Services.ProductKitServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import org.springframework.web.multipart.MultipartFile;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//@Disabled
//public class ProductKitServiceImplTest {
//
//    @Mock private ProductKitsRepository productKitsRepository;
//    @Mock private ProductKitsMapper productKitsMapper;
//    @Mock private UserRepository userRepository;
//    @Mock private ReviewsRepository reviewsRepository;
//    @Mock private CloudinaryService cloudinaryService;
//
//    @InjectMocks private ProductKitServiceImpl productKitService;
//
//    private ProductKits product;
//    private ProductKitsDTO productDTO;
//    private User user;
//    private Reviews review;
//    private ReviewsDTO reviewDTO;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        product = new ProductKits();
//        product.setProductKitId(1L);
//        product.setName("Football Kit");
//        product.setDescription("High-quality football kit");
//        product.setPrice(BigDecimal.valueOf(46.99));
//        product.setPlayerNameOnKit("Ronaldo");
//        product.setTeamNameOfKit("Manchester United");
//        product.setSize("L");
//        product.setType(ClubType.NationalClub);
//        product.setQuantity(10);
//        product.setImageUrl("image_url");
//
//        productDTO = new ProductKitsDTO(
//                1L, "Football Kit", "High-quality football kit",
//                BigDecimal.valueOf(46.99), "Ronaldo", "Manchester United",
//                "L", ClubType.NationalClub, 10, "image_url"
//        );
//
//        user = new User();
//        user.setUserId(1L);
//
//        review = new Reviews();
//        review.setReviewId(100L);
//        review.setUser(user);
//        review.setProduct(product);
//        review.setComment("Great product!");
//        review.setCreatedAt(LocalDateTime.now());
//
//        reviewDTO = new ReviewsDTO(100L, 1L, 1L, "Great product!", LocalDateTime.now());
//    }
//
//    @Test
//    void testGetProductById_Success() {
//        when(productKitsRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(productKitsMapper.toDTO(product)).thenReturn(productDTO);
//
//        Optional<ProductKitsDTO> result = productKitService.getProductById(1L);
//
//        assertTrue(result.isPresent());
//        assertEquals("Football Kit", result.get().name());
//        verify(productKitsRepository).findById(1L);
//        verify(productKitsMapper).toDTO(product);
//    }
//
//    @Test
//    void testGetAllProducts_Success() {
//        when(productKitsRepository.findAll()).thenReturn(List.of(product));
//        when(productKitsMapper.toDTO(product)).thenReturn(productDTO);
//
//        List<ProductKitsDTO> products = productKitService.getAllProducts();
//
//        assertEquals(1, products.size());
//        assertEquals("Football Kit", products.get(0).name());
//        verify(productKitsRepository).findAll();
//        verify(productKitsMapper).toDTO(product);
//    }
//
//    @Test
//    void testAddProduct_Success() throws IOException {
//        MultipartFile file = mock(MultipartFile.class);
//        when(file.isEmpty()).thenReturn(false);
//        when(cloudinaryService.uploadFile(file)).thenReturn("new_image_url");
//        when(productKitsRepository.save(any(ProductKits.class))).thenReturn(product);
//        when(productKitsMapper.toDTO(product)).thenReturn(productDTO);
//
//        ProductKitsDTO result = productKitService.addProduct(productDTO, file);
//
//        assertNotNull(result);
//        assertEquals("Football Kit", result.name());
//        verify(cloudinaryService).uploadFile(file);
//        verify(productKitsRepository).save(any(ProductKits.class));
//        verify(productKitsMapper).toDTO(product);
//    }
//
//    @Test
//    void testUpdateProduct_Success() throws IOException {
//        MultipartFile file = mock(MultipartFile.class);
//        when(file.isEmpty()).thenReturn(true); // No new image uploaded
//        when(productKitsRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(productKitsRepository.save(any(ProductKits.class))).thenReturn(product);
//        when(productKitsMapper.toDTO(product)).thenReturn(productDTO);
//
//        ProductKitsDTO result = productKitService.updateProduct(1L, productDTO, file);
//
//        assertNotNull(result);
//        assertEquals("Football Kit", result.name());
//        verify(productKitsRepository).findById(1L);
//        verify(productKitsRepository).save(any(ProductKits.class));
//        verify(productKitsMapper).toDTO(product);
//    }
//
//    @Test
//    void testDeleteProduct_Success() {
//        when(productKitsRepository.findById(1L)).thenReturn(Optional.of(product));
//
//        productKitService.deleteProduct(1L);
//
//        verify(productKitsRepository).findById(1L);
//        verify(productKitsRepository).delete(product);
//    }
//
//
//
//
//    @Test
//    void testGetAllReviewsForProduct_Success() {
//        when(productKitsRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(reviewsRepository.findAllByProduct(product)).thenReturn(List.of(review));
//
//        List<ReviewsDTO> reviews = productKitService.getAllReviewsForProduct(1L);
//
//        assertEquals(1, reviews.size());
//        assertEquals("Great product!", reviews.get(0).comment());
//        verify(reviewsRepository).findAllByProduct(product);
//    }
//
//    @Test
//    void testAddReview_Success() {
//        when(productKitsRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(reviewsRepository.save(any(Reviews.class))).thenReturn(review);
//
//        ReviewsDTO result = productKitService.addReview(1L, reviewDTO);
//
//        assertNotNull(result);
//        assertEquals("Great product!", result.comment());
//        verify(reviewsRepository).save(any(Reviews.class));
//    }
//}
//
