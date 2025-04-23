package com.example.KitShop.ServicesTests;

import com.example.KitShop.DTOs.FavouritesDTO;
import com.example.KitShop.Models.Favourite;
import com.example.KitShop.Models.ProductKits;
import com.example.KitShop.Models.User;
import com.example.KitShop.Repositories.FavouriteRepository;
import com.example.KitShop.Repositories.ProductKitsRepository;
import com.example.KitShop.Repositories.UserRepository;
import com.example.KitShop.Services.FavouriteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FavouriteServiceImplTest {

    @Mock private FavouriteRepository favouriteRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProductKitsRepository productKitsRepository;

    @InjectMocks private FavouriteServiceImpl favouriteService;

    private User user;
    private ProductKits product;
    private Favourite favourite;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);

        product = new ProductKits();
        product.setProductKitId(100L);

        favourite = new Favourite();
        favourite.setId(500L);
        favourite.setUser(user);
        favourite.setProduct(product);
    }

    @Test
    void testAddToFavourites_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productKitsRepository.findById(100L)).thenReturn(Optional.of(product));
        when(favouriteRepository.existsByUserAndProduct(user, product)).thenReturn(false);
        when(favouriteRepository.save(any(Favourite.class))).thenReturn(favourite);

        FavouritesDTO result = favouriteService.addToFavourites(1L, 100L);

        assertNotNull(result);
        assertEquals(1L, result.userId());
        assertEquals(100L, result.productKitId());
    }

    @Test
    void testRemoveFromFavourites_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productKitsRepository.findById(100L)).thenReturn(Optional.of(product));
        when(favouriteRepository.findByUserAndProduct(user, product)).thenReturn(Optional.of(favourite));

        assertDoesNotThrow(() -> favouriteService.removeFromFavourites(1L, 100L));

        verify(favouriteRepository).delete(favourite);
    }

    @Test
    void testGetAllFavouritesForUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(favouriteRepository.findAllByUser(user)).thenReturn(List.of(favourite));

        List<FavouritesDTO> result = favouriteService.getAllFavouritesForUser(1L);

        assertEquals(1, result.size());
        assertEquals(500L, result.get(0).id());
    }
}

