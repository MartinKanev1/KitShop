package com.example.KitShop.Mappers;


import com.example.KitShop.DTOs.FavouritesDTO;

import com.example.KitShop.Models.Favourite;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FavouritesMapper {

        @Mapping(target = "id", source = "dto.id")
        @Mapping(target = "user.userId", source = "dto.userId")
        @Mapping(target = "product.productKitId", source = "dto.productKitId")

        Favourite toEntity(FavouritesDTO dto);

        FavouritesDTO toDTO(Favourite favourite);
}
