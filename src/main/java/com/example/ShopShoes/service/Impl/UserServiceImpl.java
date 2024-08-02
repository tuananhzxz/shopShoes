package com.example.ShopShoes.service.Impl;

import com.example.ShopShoes.dto.ProductDTO;
import com.example.ShopShoes.dto.UserDTO;
import com.example.ShopShoes.entity.Product;
import com.example.ShopShoes.entity.User;
import com.example.ShopShoes.repository.UserRepository;
import com.example.ShopShoes.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private ModelMapper modelMapper;
    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Page<UserDTO> searchUser(String keyword, Pageable pageable) {
        Page<User> userPage = userRepository.findByUsernameContaining(keyword, pageable);
        List<UserDTO> userDTOS = userPage.getContent().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
        return new PageImpl<>(userDTOS, pageable, userPage.getTotalElements());
    }

    @Override
    public Page<UserDTO> getAllUser(Pageable pageable) {
        Page<User> productPage = userRepository.findAll(pageable);
        List<UserDTO> userDTOS = productPage.getContent().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
        return new PageImpl<>(userDTOS, pageable, productPage.getTotalElements());
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return modelMapper.map(user, UserDTO.class);
    }
}
