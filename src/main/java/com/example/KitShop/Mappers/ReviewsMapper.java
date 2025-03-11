package com.example.KitShop.Mappers;


import com.example.KitShop.DTOs.ReviewsDTO;
import com.example.KitShop.Models.Reviews;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewsMapper {


    @Mapping(target = "reviewId", source = "dto.reviewId")
    @Mapping(target = "user.userId", source = "dto.userId")
    @Mapping(target = "product.productKitId", source = "dto.productKitId")
    @Mapping(target = "comment", source = "dto.comment")
    @Mapping(target = "createdAt", source = "dto.createdAt")

    Reviews toEntity(ReviewsDTO dto);

    ReviewsDTO toDTO( Reviews reviews);
}
