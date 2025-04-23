package com.example.KitShop.Services;


import com.example.KitShop.DTOs.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    Optional<UserDTO> getUserById(Long userId);

    Long getUserIdFromEmail(String email);


    void deleteUser(Long userId);

    UserDTO updateUser(Long userId, UserDTO userDTO);

}
