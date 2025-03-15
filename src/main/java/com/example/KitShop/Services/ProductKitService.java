package com.example.KitShop.Services;


import com.example.KitShop.DTOs.ProductKitsDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductKitService {

    Optional<ProductKitsDTO> getProductById(Long productId);

    List<ProductKitsDTO> getAllProducts();

    ProductKitsDTO addProduct(ProductKitsDTO productKitsDTO);

    ProductKitsDTO updateProduct(Long productId,ProductKitsDTO productKitsDTO );

    void deleteProduct(Long productId);
}
