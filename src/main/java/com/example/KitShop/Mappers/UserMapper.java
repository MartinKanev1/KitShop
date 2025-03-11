package com.example.KitShop.Mappers;


import com.example.KitShop.DTOs.UserDTO;
import com.example.KitShop.Models.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

   @Mapping(target = "userId", source = "dto.userId")
   @Mapping(target = "firstName", source = "dto.firstName")
   @Mapping(target = "lastName", source = "dto.lastName")
   @Mapping(target = "email", source = "dto.email")
   @Mapping(target = "password", source = "dto.password")
   @Mapping(target = "phoneNumber", source = "dto.phoneNumber")
   @Mapping(target = "address", source = "dto.address")
   @Mapping(target = "role", source = "dto.role")
   User toEntity(UserDTO dto);

   UserDTO toDTO(User user);
}
