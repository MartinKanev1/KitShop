package com.example.KitShop.Repositories;

import com.example.KitShop.Models.ShoppingCart;
import com.example.KitShop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository< ShoppingCart, Long> {
    Optional<ShoppingCart> findByUser(User user);



}
