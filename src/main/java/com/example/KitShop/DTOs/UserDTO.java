package com.example.KitShop.DTOs;

import com.example.KitShop.Models.Roles;

public record UserDTO(


    Long userId,
    String firstName,
    String lastName,
    String email,
    String password,
    String address,
    String phoneNumber,
    Roles role

    )
   {
}
