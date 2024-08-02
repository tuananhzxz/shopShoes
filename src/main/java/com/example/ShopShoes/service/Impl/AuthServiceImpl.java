package com.example.ShopShoes.service.Impl;

import com.example.ShopShoes.dto.JwtAuthResponse;
import com.example.ShopShoes.dto.LoginDTO;
import com.example.ShopShoes.dto.RegisterDTO;
import com.example.ShopShoes.entity.Role;
import com.example.ShopShoes.entity.User;
import com.example.ShopShoes.exception.ShoesApiException;
import com.example.ShopShoes.repository.RoleRepository;
import com.example.ShopShoes.repository.UserRepository;
import com.example.ShopShoes.security.JwtTokenProvider;
import com.example.ShopShoes.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public String register(RegisterDTO registerDto) {

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new ShoesApiException("Username is required", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ShoesApiException("Email is required",HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setAddress(registerDto.getAddress());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setPhone(registerDto.getPhone());

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_USER");
        roles.add(role);

        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public String login(LoginDTO loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
