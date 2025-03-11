package com.example.KitShop.Mappers;

import com.example.KitShop.DTOs.CartItemDTO;
import com.example.KitShop.DTOs.OrderItemDTO;
import com.example.KitShop.Models.CartItem;
import com.example.KitShop.Models.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemMapper {

    @Mapping(target = "orderItemId", source = "dto.orderItemId")
    @Mapping(target = "product.productKitId", source = "dto.productId")
    @Mapping(target = "quantity", source = "dto.quantity")
    OrderItem toEntity(OrderItemDTO dto);

    OrderItemDTO toDTO(OrderItem orderItem);
}
