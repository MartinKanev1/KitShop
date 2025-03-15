package com.example.KitShop.Services;


import com.example.KitShop.DTOs.ProductKitsDTO;
import com.example.KitShop.Mappers.ProductKitsMapper;
import com.example.KitShop.Models.ClubType;
import com.example.KitShop.Models.ProductKits;
import com.example.KitShop.Repositories.ProductKitsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductKitServiceImpl implements ProductKitService {

    @Autowired
    private final ProductKitsRepository productKitsRepository;

    @Autowired
    private final ProductKitsMapper productKitsMapper;

    public ProductKitServiceImpl(ProductKitsRepository productKitsRepository,ProductKitsMapper productKitsMapper) {
        this.productKitsRepository = productKitsRepository;
        this.productKitsMapper = productKitsMapper;
    }

    public Optional<ProductKitsDTO> getProductById( Long productId) {
        Optional<ProductKits> product = productKitsRepository.findById(productId);
        return product.map(productKitsMapper::toDTO);
    }

    public List<ProductKitsDTO> getAllProducts() {
        return productKitsRepository.findAll().stream().map(productKitsMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ProductKitsDTO addProduct(ProductKitsDTO productKitsDTO) {
        ProductKits productKits = new ProductKits();
        productKits.setName(productKitsDTO.name());
        productKits.setDescription(productKitsDTO.description());
        productKits.setPrice(productKitsDTO.price());
        productKits.setPlayerNameOnKit(productKitsDTO.playerNameOnKit());
        productKits.setTeamNameOfKit(productKitsDTO.teamNameOfKit());
        productKits.setSize(productKitsDTO.size());
        productKits.setType(productKitsDTO.type());
        productKits.setQuantity(productKitsDTO.quantity());
        productKits.setImageUrl(productKitsDTO.imageUrl());

        ProductKits newProduct = productKitsRepository.save(productKits);

        ProductKitsDTO createdProduct = productKitsMapper.toDTO(newProduct);

        return createdProduct;

    }

    @Transactional
    public ProductKitsDTO updateProduct(Long productId,ProductKitsDTO productKitsDTO ) {
        ProductKits currentProduct = productKitsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        currentProduct.setName(productKitsDTO.name());
        currentProduct.setDescription(productKitsDTO.description());
        currentProduct.setPrice(productKitsDTO.price());
        currentProduct.setPlayerNameOnKit(productKitsDTO.playerNameOnKit());
        currentProduct.setTeamNameOfKit(productKitsDTO.teamNameOfKit());
        currentProduct.setSize(productKitsDTO.size());
        currentProduct.setType(productKitsDTO.type());
        currentProduct.setQuantity(productKitsDTO.quantity());
        currentProduct.setImageUrl(productKitsDTO.imageUrl());

        ProductKits updatedProduct = productKitsRepository.save(currentProduct);


        return productKitsMapper.toDTO(updatedProduct);
    }

    public void deleteProduct(Long productId) {

        ProductKits currentProduct = productKitsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        productKitsRepository.delete(currentProduct);

    }


    @Cacheable(value = "searchProductsCache", key = "#keyword")
    public List<ProductKitsDTO> searchProductKits(String keyword) {
      return productKitsRepository.searchProducts(keyword);

    }

}
