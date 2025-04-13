package com.example.KitShop.Services;

import com.example.KitShop.DTOs.FavouritesDTO;
import com.example.KitShop.Models.Favourite;
import com.example.KitShop.Models.ProductKits;
import com.example.KitShop.Models.User;
import com.example.KitShop.Repositories.FavouriteRepository;
import com.example.KitShop.Repositories.ProductKitsRepository;
import com.example.KitShop.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavouriteServiceImpl implements FavouriteService{

    private final FavouriteRepository favouriteRepository;
    private final UserRepository userRepository;
    private final ProductKitsRepository productKitsRepository;

    public FavouriteServiceImpl( FavouriteRepository favouriteRepository,UserRepository userRepository,ProductKitsRepository productKitsRepository) {

        this.favouriteRepository = favouriteRepository;
        this.userRepository=userRepository;
        this.productKitsRepository=productKitsRepository;
    }

    @Transactional
    public FavouritesDTO addToFavourites(Long userId, Long productKitId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

        ProductKits product = productKitsRepository.findById(productKitId)
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productKitId + " not found"));


        boolean alreadyExists = favouriteRepository.existsByUserAndProduct(user, product);
        if (alreadyExists) {
            throw new IllegalStateException("This product is already in the user's favorites");
        }


        Favourite favourite =  new Favourite();
        favourite.setUser(user);
        favourite.setProduct(product);

        Favourite savedFavourite = favouriteRepository.save(favourite);

        return new FavouritesDTO(savedFavourite.getId(), userId, productKitId);
    }

    @Transactional
    public void removeFromFavourites(Long userId, Long productKitId ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

        ProductKits product = productKitsRepository.findById(productKitId)
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productKitId + " not found"));

        Favourite favourite = favouriteRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new IllegalArgumentException("Favorite not found for user ID " + userId + " and product ID " + productKitId));

        favouriteRepository.delete(favourite);

    }

    public List<FavouritesDTO> getAllFavouritesForUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

        List<Favourite> favourites = favouriteRepository.findAllByUser(user);

        return favourites.stream()
                .map(fav -> new FavouritesDTO(fav.getId(), userId, fav.getProduct().getProductKitId()))
                .collect(Collectors.toList());
    }



}
