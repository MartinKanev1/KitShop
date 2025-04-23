package com.example.KitShop.ServicesTests;

import com.example.KitShop.DTOs.UserDTO;
import com.example.KitShop.Mappers.UserMapper;
import com.example.KitShop.Models.Roles;
import com.example.KitShop.Models.User;
import com.example.KitShop.Repositories.UserRepository;
import com.example.KitShop.Services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setPassword("secret");
        user.setPhoneNumber("123456789");

        userDTO = new UserDTO(1L,"John", "Doe", "john@example.com", "secret", "123456789", Roles.Users);
    }

    @Test
    void testGetUserById_UserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        Optional<UserDTO> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("john@example.com", result.get().email());
        verify(userRepository).findById(1L);
        verify(userMapper).toDTO(user);
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserDTO> result = userService.getUserById(1L);

        assertFalse(result.isPresent());
        verify(userRepository).findById(1L);
        verify(userMapper, never()).toDTO(any());
    }

    @Test
    void testGetUserIdFromEmail_UserExists() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        Long userId = userService.getUserIdFromEmail("john@example.com");

        assertEquals(1L, userId);
        verify(userRepository).findByEmail("john@example.com");
    }

    @Test
    void testGetUserIdFromEmail_UserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserIdFromEmail("unknown@example.com"));

        assertEquals("User not found for email: unknown@example.com", exception.getMessage());
        verify(userRepository).findByEmail("unknown@example.com");
    }

    @Test
    void testUpdateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO updated = userService.updateUser(1L, userDTO);

        assertNotNull(updated);
        assertEquals("John", updated.firstName());
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
        verify(userMapper).toDTO(user);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(1L, userDTO));

        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUser(1L));

        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository, never()).delete(any());
    }
}
