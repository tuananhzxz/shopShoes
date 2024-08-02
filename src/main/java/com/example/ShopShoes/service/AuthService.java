package com.example.ShopShoes.service;

import com.example.ShopShoes.dto.JwtAuthResponse;
import com.example.ShopShoes.dto.LoginDTO;
import com.example.ShopShoes.dto.RegisterDTO;

public interface AuthService {

    String register(RegisterDTO registerDto);
    String login(LoginDTO loginDto);

    void logout();
}
