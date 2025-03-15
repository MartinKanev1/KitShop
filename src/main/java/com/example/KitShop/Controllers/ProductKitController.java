package com.example.KitShop.Controllers;

import com.example.KitShop.DTOs.ProductKitsDTO;
import com.example.KitShop.Models.ClubType;
import com.example.KitShop.Services.ProductKitServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<ProductKitsDTO> addProduct(@RequestBody ProductKitsDTO productKitsDTO) {
        ProductKitsDTO createdProduct = productKitService.addProduct(productKitsDTO);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductKitsDTO> updateProduct(@PathVariable Long id, @RequestBody ProductKitsDTO productKitsDTO) {
        try {
            ProductKitsDTO updatedProduct = productKitService.updateProduct(id, productKitsDTO);
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

}
