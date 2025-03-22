package com.example.KitShop.Services;


import com.example.KitShop.DTOs.ProductKitsDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface ProductKitService {

    Optional<ProductKitsDTO> getProductById(Long productId);

    List<ProductKitsDTO> getAllProducts();

    ProductKitsDTO addProduct(ProductKitsDTO productKitsDTO, MultipartFile file) throws IOException;

    ProductKitsDTO updateProduct(Long productId,ProductKitsDTO productKitsDTO, MultipartFile file) throws IOException;

    void deleteProduct(Long productId);
}
