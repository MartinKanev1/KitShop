package com.example.KitShop.DTOs;

import com.example.KitShop.Models.ClubType;

import java.math.BigDecimal;

public record ProductKitsDTO(

        Long productKitId,
        String name,
        String description,
        BigDecimal price,
        String playerNameOnKit,
        String teamNameOfKit,
        String size,
        ClubType type,
        int quantity,
        String imageUrl
) {
}
