package com.example.ShopShoes.service;

import com.example.ShopShoes.dto.UserDTO;
import com.example.ShopShoes.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDTO getUserById(Long userId);
    List<UserDTO> getAllUsers();
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long userId, UserDTO userDTO);
    void deleteUser(Long userId);

    Page<UserDTO> searchUser(String keyword, Pageable pageable);

    Page<UserDTO> getAllUser(Pageable pageable);

    UserDTO getUserByUsername(String username);
}
