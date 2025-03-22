package com.example.KitShop.ControllersTests;

import com.example.KitShop.Controllers.ProductKitController;
import com.example.KitShop.DTOs.ProductKitsDTO;
import com.example.KitShop.DTOs.ReviewsDTO;
import com.example.KitShop.Models.ClubType;
import com.example.KitShop.Services.ProductKitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductKitControllerTest {

    private MockMvc mockMvc;

    @Mock private ProductKitServiceImpl productKitService;

    @InjectMocks private ProductKitController productKitController;

    private ProductKitsDTO productDTO;
    private ReviewsDTO reviewDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productKitController).build();

        productDTO = new ProductKitsDTO(
                1L, "Football Kit", "High-quality football kit",
                BigDecimal.valueOf(49.99), "Ronaldo", "Manchester United",
                "L", ClubType.NationalClub, 10, "image_url"
        );

        reviewDTO = new ReviewsDTO(100L, 1L, 1L, "Great product!", LocalDateTime.now());
    }

    @Test
    void testGetProductById_Success() throws Exception {
        when(productKitService.getProductById(1L)).thenReturn(Optional.of(productDTO));

        mockMvc.perform(get("/api/product-kits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productKitId").value(1))
                .andExpect(jsonPath("$.name").value("Football Kit"));

        verify(productKitService).getProductById(1L);
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        when(productKitService.getProductById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/product-kits/1"))
                .andExpect(status().isNotFound());

        verify(productKitService).getProductById(1L);
    }

    @Test
    void testGetAllProducts_Success() throws Exception {
        when(productKitService.getAllProducts()).thenReturn(List.of(productDTO));

        mockMvc.perform(get("/api/product-kits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Football Kit"));

        verify(productKitService).getAllProducts();
    }

    @Test
    void testAddProduct_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "mock image content".getBytes());
        MockMultipartFile productDTOJson = new MockMultipartFile("productKitsDTO", "",
                "application/json", ("{" +
                "\"productKitId\":1," +
                "\"name\":\"Football Kit\"," +
                "\"description\":\"High-quality football kit\"," +
                "\"price\":49.99," +
                "\"playerNameOnKit\":\"Ronaldo\"," +
                "\"teamNameOfKit\":\"Manchester United\"," +
                "\"size\":\"L\"," +
                "\"type\":\"PREMIER_LEAGUE\"," +
                "\"quantity\":10," +
                "\"imageUrl\":\"image_url\"}").getBytes());

        when(productKitService.addProduct(any(ProductKitsDTO.class), any())).thenReturn(productDTO);

        mockMvc.perform(multipart("/api/product-kits")
                        .file(file)
                        .file(productDTOJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Football Kit"));

        verify(productKitService).addProduct(any(ProductKitsDTO.class), any());
    }

    @Test
    void testUpdateProduct_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "mock image content".getBytes());
        MockMultipartFile productDTOJson = new MockMultipartFile("productKitsDTO", "",
                "application/json", ("{" +
                "\"productKitId\":1," +
                "\"name\":\"Updated Kit\"," +
                "\"description\":\"Updated description\"," +
                "\"price\":59.99," +
                "\"playerNameOnKit\":\"Messi\"," +
                "\"teamNameOfKit\":\"Barcelona\"," +
                "\"size\":\"M\"," +
                "\"type\":\"LA_LIGA\"," +
                "\"quantity\":5," +
                "\"imageUrl\":\"new_image_url\"}").getBytes());

        when(productKitService.updateProduct(eq(1L), any(ProductKitsDTO.class), any())).thenReturn(productDTO);

        mockMvc.perform(multipart("/api/product-kits/1")
                        .file(file)
                        .file(productDTOJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT"); // Simulate PUT request
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Football Kit"));

        verify(productKitService).updateProduct(eq(1L), any(ProductKitsDTO.class), any());
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        doNothing().when(productKitService).deleteProduct(1L);

        mockMvc.perform(delete("/api/product-kits/1"))
                .andExpect(status().isNoContent());

        verify(productKitService).deleteProduct(1L);
    }

    @Test
    void testSearchProductKits_Success() throws Exception {
        when(productKitService.searchProductKits("kit")).thenReturn(List.of(productDTO));

        mockMvc.perform(get("/api/product-kits/search")
                        .param("keyword", "kit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Football Kit"));

        verify(productKitService).searchProductKits("kit");
    }

    @Test
    void testAddReview_Success() throws Exception {
        when(productKitService.addReview(eq(1L), any(ReviewsDTO.class))).thenReturn(reviewDTO);

        mockMvc.perform(post("/api/product-kits/1/addReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reviewId\":100,\"userId\":1,\"productId\":1,\"comment\":\"Great product!\",\"createdAt\":\"2025-03-21T12:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment").value("Great product!"));

        verify(productKitService).addReview(eq(1L), any(ReviewsDTO.class));
    }

    @Test
    void testGetAllReviewsForProduct_Success() throws Exception {
        when(productKitService.getAllReviewsForProduct(1L)).thenReturn(List.of(reviewDTO));

        mockMvc.perform(get("/api/product-kits/1/getReviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].comment").value("Great product!"));

        verify(productKitService).getAllReviewsForProduct(1L);
    }
}
