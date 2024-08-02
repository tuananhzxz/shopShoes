package com.example.ShopShoes.controller.Auth;

import com.example.ShopShoes.dto.JwtAuthResponse;
import com.example.ShopShoes.dto.LoginDTO;
import com.example.ShopShoes.dto.RegisterDTO;
import com.example.ShopShoes.repository.UserRepository;
import com.example.ShopShoes.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid RegisterDTO registerDto, Model model) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            model.addAttribute("errorMessage", "Username already exists!");
            return "register";
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            model.addAttribute("errorMessage", "Email already exists!");
            return "register";
        }
        authService.register(registerDto);
        model.addAttribute("successMessage", "Registration successful!");
        return "register";
    }

    @GetMapping("/logon")
    public String loginpage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/admin";
        }
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping("/logon")
    public String loginUser(@Valid @ModelAttribute("loginDTO") LoginDTO loginDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "login";
        }
        try {
            authService.login(loginDto);
            model.addAttribute("successLogin", "Login successful!");
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("loginDTO", loginDto);
            model.addAttribute("errorLogin", "Invalid username or password. Please try again.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        authService.logout();
        return "redirect:/logon";
    }
}
