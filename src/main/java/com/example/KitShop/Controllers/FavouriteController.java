package com.example.KitShop.Controllers;

import com.example.KitShop.DTOs.FavouritesDTO;
import com.example.KitShop.Services.FavouriteServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favourites")
public class FavouriteController {

    private final FavouriteServiceImpl favouriteServiceImpl;

    public FavouriteController(FavouriteServiceImpl favouriteServiceImpl) {
        this.favouriteServiceImpl = favouriteServiceImpl;
    }


    @PostMapping
    public ResponseEntity<FavouritesDTO> addToFavourites(
            @RequestParam Long userId,
            @RequestParam Long productKitId) {
        FavouritesDTO dto = favouriteServiceImpl.addToFavourites(userId, productKitId);
        return ResponseEntity.ok(dto);
    }


    @DeleteMapping
    public ResponseEntity<Void> removeFromFavourites(
            @RequestParam Long userId,
            @RequestParam Long productKitId) {
        favouriteServiceImpl.removeFromFavourites(userId, productKitId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{userId}")
    public ResponseEntity<List<FavouritesDTO>> getAllFavouritesForUser(@PathVariable Long userId) {
        List<FavouritesDTO> favourites = favouriteServiceImpl.getAllFavouritesForUser(userId);
        return ResponseEntity.ok(favourites);
    }
}
