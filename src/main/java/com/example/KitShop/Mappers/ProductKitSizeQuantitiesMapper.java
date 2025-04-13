package com.example.KitShop.Mappers;


import com.example.KitShop.DTOs.ProductKitSizeQuantitiesDTO;
import com.example.KitShop.Models.ProductKitSizeQuantities;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductKitSizeQuantitiesMapper {

    @Mapping(target = "productKitSizeQuantityId", source = "dto.productKitSizeQuantityId")
    @Mapping(target = "size", source = "dto.size")
    @Mapping(target = "quantity", source = "dto.quantity")

    ProductKitSizeQuantities toEntity(ProductKitSizeQuantitiesDTO dto);

    ProductKitSizeQuantitiesDTO toDto( ProductKitSizeQuantities productKitSizeQuantities);
}
