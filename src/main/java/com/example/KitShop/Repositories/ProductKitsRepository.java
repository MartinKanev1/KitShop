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


//@Query("SELECT new com.example.KitShop.DTOs.ProductKitsDTO(" +
//        "p.productKitId, p.name, p.description, p.price, p.playerNameOnKit, " +
//        "p.teamNameOfKit, p.size, p.type, p.quantity, p.imageUrl) " +
//        "FROM ProductKits p " +
//        "WHERE (" +
//        "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//        "OR LOWER(p.playerNameOnKit) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//        "OR LOWER(p.teamNameOfKit) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
//      ") ")

    //List<ProductKitsDTO> searchProducts(String keyword);

    @Query("""
    SELECT DISTINCT p FROM ProductKits p
    LEFT JOIN FETCH p.sizes
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(p.playerNameOnKit) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(p.teamNameOfKit) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    List<ProductKits> searchProducts(String keyword);

}
