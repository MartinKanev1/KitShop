package com.example.KitShop.Repositories;

import com.example.KitShop.Models.CartItem;
import com.example.KitShop.Models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository< CartItem, Long> {






    List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);



    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.shoppingCart.shoppingCartId = :shoppingCartId")
    void deleteByShoppingCart_ShoppingCartId(Long shoppingCartId);



}
