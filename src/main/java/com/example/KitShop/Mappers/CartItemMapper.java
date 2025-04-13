package com.example.KitShop.Mappers;

import com.example.KitShop.DTOs.CartItemDTO;
import com.example.KitShop.Models.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartItemMapper {

    @Mapping(target = "cartItemId", source = "dto.cartItemId")
    @Mapping(target = "product.productKitId", source = "dto.productId")
    @Mapping(target = "quantity", source = "dto.quantity")
    @Mapping(target = "size", source = "dto.size")

    CartItem toEntity( CartItemDTO dto);

    CartItemDTO toDTO( CartItem cartItem);
}
