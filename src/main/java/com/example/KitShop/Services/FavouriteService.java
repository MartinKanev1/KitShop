package com.example.KitShop.Services;


import com.example.KitShop.DTOs.FavouritesDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FavouriteService {

    FavouritesDTO addToFavourites(Long userId, Long productKitId);

    void removeFromFavourites(Long userId, Long productKitId);

    List<FavouritesDTO> getAllFavouritesForUser(Long userId);
}
