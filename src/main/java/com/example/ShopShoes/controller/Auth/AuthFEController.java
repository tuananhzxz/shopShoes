package com.example.ShopShoes.controller.Auth;

import com.example.ShopShoes.dto.JwtAuthResponse;
import com.example.ShopShoes.dto.LoginDTO;
import com.example.ShopShoes.dto.RegisterDTO;
import com.example.ShopShoes.repository.UserRepository;
import com.example.ShopShoes.security.JwtTokenProvider;
import com.example.ShopShoes.service.AuthService;
import com.example.ShopShoes.service.Cookie.CookieService;
import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping
@AllArgsConstructor
public class AuthFEController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/signup")
    public String register(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "fe/signup";
    }

    @PostMapping("/signup")
    public String registerUser(@Valid RegisterDTO registerDto, Model model) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            model.addAttribute("errorMessage", "Tên đăng nhập đã sử dụng!");
            return "fe/signup";
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            model.addAttribute("errorMessage", "Email đã tồn tại!");
            return "fe/signup";
        }
        authService.register(registerDto);
        model.addAttribute("successMessage", "Đăng ký thành công!");
        return "fe/signup";
    }

    @GetMapping("/login")
    public String login(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/";
        }
        model.addAttribute("loginDTO", new LoginDTO());
        return "fe/login";
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute("loginDTO") LoginDTO loginDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "fe/login";
        }
        try {
            model.addAttribute("successLogin", "Đăng nhập thành công!");
            return "fe/login";
        } catch (Exception e) {
            model.addAttribute("loginDTO", loginDto);
            model.addAttribute("errorLogin", "Sai tên đăng nhập hoặc mật khẩu. Vui lòng thử lại.");
            return "fe/login";
        }
    }

    @GetMapping("/log")
    public String logout() {
        authService.logout();
        return "redirect:/login";
    }
}
