package com.example.KitShop.ControllersTests;

import com.example.KitShop.Controllers.UserController;
import com.example.KitShop.DTOs.UserDTO;
import com.example.KitShop.Models.Roles;
import com.example.KitShop.Services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock private UserServiceImpl userServiceImpl;

    @InjectMocks private UserController userController;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        userDTO = new UserDTO(1L, "John", "Doe", "john@example.com", "password123", "123456789", Roles.Users);
    }

    @Test
    void testGetUserById_Success() throws Exception {
        when(userServiceImpl.getUserById(1L)).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(userServiceImpl).getUserById(1L);
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userServiceImpl.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(userServiceImpl).getUserById(1L);
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        doNothing().when(userServiceImpl).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userServiceImpl).deleteUser(1L);
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        when(userServiceImpl.updateUser(eq(1L), any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john@example.com\", \"password\": \"password123\", \"phoneNumber\": \"123456789\", \"role\": \"Users\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(userServiceImpl).updateUser(eq(1L), any(UserDTO.class));
    }

    @Test
    void testGetUserIdByEmail_Success() throws Exception {
        when(userServiceImpl.getUserIdFromEmail("john@example.com")).thenReturn(1L);

        mockMvc.perform(get("/api/users/id-by-email/john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));

        verify(userServiceImpl).getUserIdFromEmail("john@example.com");
    }

    @Test
    void testGetUserIdByEmail_NotFound() throws Exception {
        when(userServiceImpl.getUserIdFromEmail("unknown@example.com"))
                .thenThrow(new RuntimeException("User not found for email: unknown@example.com"));

        mockMvc.perform(get("/api/users/id-by-email/unknown@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found for email: unknown@example.com"));

        verify(userServiceImpl).getUserIdFromEmail("unknown@example.com");
    }
}
