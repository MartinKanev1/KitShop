package com.example.KitShop.ControllersTests;

import com.example.KitShop.Controllers.FavouriteController;
import com.example.KitShop.DTOs.FavouritesDTO;
import com.example.KitShop.Services.FavouriteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FavouriteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FavouriteServiceImpl favouriteServiceImpl;

    @InjectMocks
    private FavouriteController favouriteController;

    private FavouritesDTO favouriteDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(favouriteController).build();

        favouriteDTO = new FavouritesDTO(
                1L,
                1L,
                100L

        );
    }

    @Test
    void testAddToFavourites_Success() throws Exception {
        when(favouriteServiceImpl.addToFavourites(1L, 100L)).thenReturn(favouriteDTO);

        mockMvc.perform(post("/api/favourites")
                        .param("userId", "1")
                        .param("productKitId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.id").value(100));

        verify(favouriteServiceImpl).addToFavourites(1L, 100L);
    }

    @Test
    void testRemoveFromFavourites_Success() throws Exception {
        doNothing().when(favouriteServiceImpl).removeFromFavourites(1L, 100L);

        mockMvc.perform(delete("/api/favourites")
                        .param("userId", "1")
                        .param("productKitId", "100"))
                .andExpect(status().isNoContent());

        verify(favouriteServiceImpl).removeFromFavourites(1L, 100L);
    }

    @Test
    void testGetAllFavouritesForUser_Success() throws Exception {
        when(favouriteServiceImpl.getAllFavouritesForUser(1L)).thenReturn(List.of(favouriteDTO));

        mockMvc.perform(get("/api/favourites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].productKitId").value(100));

        verify(favouriteServiceImpl).getAllFavouritesForUser(1L);
    }
}
