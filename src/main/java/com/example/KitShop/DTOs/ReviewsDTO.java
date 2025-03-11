package com.example.KitShop.DTOs;

import java.time.LocalDateTime;

public record ReviewsDTO(
        Long reviewId,
        Long userId,
        Long productKitId,
        String comment,
        LocalDateTime createdAt
) {
}
