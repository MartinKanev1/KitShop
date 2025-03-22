package com.example.KitShop.Repositories;

import com.example.KitShop.Models.ProductKits;
import com.example.KitShop.Models.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    List<Reviews> findAllByProduct(ProductKits product);
}
