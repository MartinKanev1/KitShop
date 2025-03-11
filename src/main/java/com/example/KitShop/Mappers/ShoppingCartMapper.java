package com.example.KitShop.Mappers;

import com.example.KitShop.DTOs.ShoppingCartDTO;
import com.example.KitShop.Models.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShoppingCartMapper {
    @Mapping(target = "shoppingCartId", source = "dto.shoppingCartId")
    @Mapping(target = "user.userId", source = "dto.userId")
    @Mapping(target = "cartItems", source = "dto.cartItems")

    ShoppingCart toEntity(ShoppingCartDTO dto);

    ShoppingCartDTO toDTO (ShoppingCart shoppingCart);


}
