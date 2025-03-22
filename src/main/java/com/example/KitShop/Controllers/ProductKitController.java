package com.example.KitShop.Controllers;

import com.example.KitShop.DTOs.ProductKitsDTO;
import com.example.KitShop.DTOs.ReviewsDTO;
import com.example.KitShop.Models.ClubType;
import com.example.KitShop.Services.ProductKitServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/product-kits")
public class ProductKitController {

    private final ProductKitServiceImpl productKitService;

    @Autowired
    public ProductKitController(ProductKitServiceImpl productKitService) {
        this.productKitService = productKitService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductKitsDTO> getProductById(@PathVariable Long id) {
        return productKitService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductKitsDTO>> getAllProducts() {
        List<ProductKitsDTO> products = productKitService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductKitsDTO> addProduct(@RequestPart ProductKitsDTO productKitsDTO,@RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            System.out.println(" File is NULL or EMPTY!");
        } else {
            System.out.println(" File received: " + file.getOriginalFilename());
        }

        ProductKitsDTO createdProduct = productKitService.addProduct(productKitsDTO, file );
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping(value ="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductKitsDTO> updateProduct(@PathVariable Long id, @RequestPart ProductKitsDTO productKitsDTO,
                                                        @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        try {
            ProductKitsDTO updatedProduct = productKitService.updateProduct(id, productKitsDTO,file);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productKitService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<ProductKitsDTO> searchProductKits(
            @RequestParam String keyword)
             {
        return productKitService.searchProductKits(keyword);
    }

    @PostMapping("/{productId}/addReview")
    public ResponseEntity<ReviewsDTO> addReview(@PathVariable Long productId, @RequestBody ReviewsDTO reviewsDTO) {
        ReviewsDTO reviews = productKitService.addReview(productId, reviewsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviews);
    }

    @GetMapping("/{productId}/getReviews")
    public ResponseEntity<List<ReviewsDTO>> getAllReviewsForProduct(@PathVariable Long productId) {
        List<ReviewsDTO> reviews = productKitService.getAllReviewsForProduct(productId);
        return ResponseEntity.ok(reviews);
    }

}
