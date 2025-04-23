package com.example.KitShop.Services;


import com.example.KitShop.DTOs.UserDTO;
import com.example.KitShop.Mappers.UserMapper;
import com.example.KitShop.Models.Roles;
import com.example.KitShop.Models.User;
import com.example.KitShop.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements  UserService{

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Optional<UserDTO> getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(userMapper::toDTO);
    }

    public Long getUserIdFromEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getUserId)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));
    }




    @Transactional
    public UserDTO updateUser(Long userId, UserDTO updatedUserDTO) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        existingUser.setFirstName(updatedUserDTO.firstName());
        existingUser.setLastName(updatedUserDTO.lastName());
        existingUser.setEmail(updatedUserDTO.email());
        existingUser.setPassword(updatedUserDTO.password());
        existingUser.setPhoneNumber(updatedUserDTO.phoneNumber());


        User savedUser = userRepository.save(existingUser);
        return userMapper.toDTO(savedUser);
    }

    public void deleteUser(Long userId) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));


        userRepository.delete(existingUser);
    }




}
