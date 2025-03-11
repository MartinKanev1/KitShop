package com.example.KitShop.Mappers;

import com.example.KitShop.DTOs.ProductKitsDTO;
import com.example.KitShop.Models.ProductKits;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductKitsMapper {

    @Mapping(target = "productKitId", source = "dto.productKitId")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "price", source = "dto.price")
    @Mapping(target = "playerNameOnKit", source = "dto.playerNameOnKit")
    @Mapping(target = "teamNameOfKit", source = "dto.teamNameOfKit")
    @Mapping(target = "type", source = "dto.type")
    @Mapping(target = "imageUrl", source = "dto.imageUrl")

    ProductKits toEntity(ProductKitsDTO dto);

    ProductKitsDTO toDTO(ProductKits productKits);
}
