package com.example.KitShop.Repositories;

import com.example.KitShop.DTOs.ProductKitsDTO;
import com.example.KitShop.Models.ClubType;
import com.example.KitShop.Models.ProductKits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductKitsRepository extends JpaRepository<ProductKits, Long> {



    @Query("""
    SELECT DISTINCT p FROM ProductKits p
    LEFT JOIN FETCH p.sizes
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(p.playerNameOnKit) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(p.teamNameOfKit) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    List<ProductKits> searchProducts(String keyword);

}
