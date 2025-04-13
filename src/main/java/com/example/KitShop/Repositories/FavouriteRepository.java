package com.example.KitShop.Repositories;

import com.example.KitShop.Models.Favourite;
import com.example.KitShop.Models.ProductKits;
import com.example.KitShop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

    boolean existsByUserAndProduct(User user, ProductKits product);

    Optional<Favourite> findByUserAndProduct(User user, ProductKits product);

    List<Favourite> findAllByUser(User user);


}
