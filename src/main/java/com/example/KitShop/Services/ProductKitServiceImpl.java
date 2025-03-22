package com.example.KitShop.Services;


import com.example.KitShop.DTOs.ProductKitsDTO;
import com.example.KitShop.DTOs.ReviewsDTO;
import com.example.KitShop.Mappers.ProductKitsMapper;
import com.example.KitShop.Models.ClubType;
import com.example.KitShop.Models.ProductKits;
import com.example.KitShop.Models.Reviews;
import com.example.KitShop.Models.User;
import com.example.KitShop.Repositories.ProductKitsRepository;
import com.example.KitShop.Repositories.ReviewsRepository;
import com.example.KitShop.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductKitServiceImpl implements ProductKitService {

    @Autowired
    private final ProductKitsRepository productKitsRepository;

    @Autowired
    private final ProductKitsMapper productKitsMapper;

    private final UserRepository userRepository;

    private final ReviewsRepository reviewsRepository;

    private final CloudinaryService cloudinaryService;

    public ProductKitServiceImpl(ProductKitsRepository productKitsRepository,ProductKitsMapper productKitsMapper,UserRepository userRepository,ReviewsRepository reviewsRepository,CloudinaryService cloudinaryService) {
        this.productKitsRepository = productKitsRepository;
        this.productKitsMapper = productKitsMapper;
        this.userRepository=userRepository;
        this.reviewsRepository=reviewsRepository;
        this.cloudinaryService=cloudinaryService;
    }

    public Optional<ProductKitsDTO> getProductById( Long productId) {
        Optional<ProductKits> product = productKitsRepository.findById(productId);
        return product.map(productKitsMapper::toDTO);
    }

    public List<ProductKitsDTO> getAllProducts() {
        return productKitsRepository.findAll().stream().map(productKitsMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ProductKitsDTO addProduct(ProductKitsDTO productKitsDTO, MultipartFile file) throws IOException {

        String imageUrl = null;
        if (file != null && !file.isEmpty()) {
            imageUrl = cloudinaryService.uploadFile(file);
        }

        System.out.println("🛠 Saving product with image URL: " + imageUrl);

        ProductKits productKits = new ProductKits();
        productKits.setName(productKitsDTO.name());
        productKits.setDescription(productKitsDTO.description());
        productKits.setPrice(productKitsDTO.price());
        productKits.setPlayerNameOnKit(productKitsDTO.playerNameOnKit());
        productKits.setTeamNameOfKit(productKitsDTO.teamNameOfKit());
        productKits.setSize(productKitsDTO.size());
        productKits.setType(productKitsDTO.type());
        productKits.setQuantity(productKitsDTO.quantity());
        productKits.setImageUrl(imageUrl);

        ProductKits newProduct = productKitsRepository.save(productKits);

        ProductKitsDTO createdProduct = productKitsMapper.toDTO(newProduct);

        return createdProduct;

    }

    @Transactional
    public ProductKitsDTO updateProduct(Long productId,ProductKitsDTO productKitsDTO,MultipartFile file ) throws IOException{
        ProductKits currentProduct = productKitsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));


        String imageUrl = currentProduct.getImageUrl(); // Keep existing image if no new file
        if (file != null && !file.isEmpty()) {
            System.out.println("🔄 Uploading new image for product ID: " + productId);
            imageUrl = cloudinaryService.uploadFile(file); // Upload new image
        }

        currentProduct.setName(productKitsDTO.name());
        currentProduct.setDescription(productKitsDTO.description());
        currentProduct.setPrice(productKitsDTO.price());
        currentProduct.setPlayerNameOnKit(productKitsDTO.playerNameOnKit());
        currentProduct.setTeamNameOfKit(productKitsDTO.teamNameOfKit());
        currentProduct.setSize(productKitsDTO.size());
        currentProduct.setType(productKitsDTO.type());
        currentProduct.setQuantity(productKitsDTO.quantity());
        currentProduct.setImageUrl(imageUrl);

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


    public List<ReviewsDTO> getAllReviewsForProduct(Long productId) {
        ProductKits product = productKitsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Reviews> reviews = reviewsRepository.findAllByProduct(product);


        return reviews.stream()
                .map(review -> new ReviewsDTO(
                        review.getReviewId(),
                        review.getUser().getUserId(),
                        review.getProduct().getProductKitId(),
                        review.getComment(),
                        review.getCreatedAt()
                ))
                .collect(Collectors.toList());


    }


    @Transactional
    public ReviewsDTO addReview(Long productId, ReviewsDTO reviewsDTO) {
        ProductKits product = productKitsRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepository.findById(reviewsDTO.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reviews reviews = new Reviews();
        reviews.setComment(reviewsDTO.comment());
        reviews.setProduct(product);
        reviews.setUser(user);
        reviews.setCreatedAt(LocalDateTime.now());

        Reviews savedReview = reviewsRepository.save(reviews);

        return new ReviewsDTO(
                savedReview.getReviewId(),
                savedReview.getProduct().getProductKitId(),
                savedReview.getUser().getUserId(),
                savedReview.getComment(),
                savedReview.getCreatedAt()

        );


    }


}
