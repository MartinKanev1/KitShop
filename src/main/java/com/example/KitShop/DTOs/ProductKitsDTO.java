package com.example.KitShop.DTOs;

import com.example.KitShop.Models.ClubType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ProductKitsDTO(

        Long productKitId,
        String name,
        String description,
        BigDecimal price,
        String playerNameOnKit,
        String teamNameOfKit,
        ClubType type,
        List<ProductKitSizeQuantitiesDTO> sizes,
        String imageUrl
) {
}
