package com.example.KitShop.Mappers;

import com.example.KitShop.DTOs.OrdersDTO;
import com.example.KitShop.Models.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrdersMapper {

    @Mapping(target = "orderId", source = "dto.orderId")
    @Mapping(target = "user.userId", source = "dto.userId")
    @Mapping(target = "orderItems", source = "dto.orderItems")
    @Mapping(target = "status", source = "dto.status")
    @Mapping(target = "totalAmount", source = "dto.totalAmount")
    @Mapping(target = "createdAt", source = "dto.createdAt")

    Orders toEntity(OrdersDTO dto);

    OrdersDTO toDTO( Orders orders);
}
