package com.example.ShopShoes.controller.User;

import com.example.ShopShoes.dto.UserDTO;
import com.example.ShopShoes.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@Controller
@AllArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String user(Model model,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> userPage;
        if (keyword != null && !keyword.isEmpty()) {
            userPage = userService.searchUser(keyword, pageable);
        } else {
            userPage = userService.getAllUser(pageable);
        }
        model.addAttribute("userPage", userPage);
        model.addAttribute("keyword", keyword);

        return "user/index";
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "user/add";
    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            return "user/add";
        }
        try {
            userService.createUser(userDTO);
            model.addAttribute("successMessage", "User added successfully");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "user/add";
    }

    @GetMapping("/edit/{userId}")
    public String editUser(@PathVariable Long userId, Model model) {
        UserDTO userDTO = userService.getUserById(userId);
        model.addAttribute("userDTO", userDTO);
        return "user/edit";
    }

    @PostMapping("/edit/{userId}")
    public String updateUser(@PathVariable Long userId,
                             @ModelAttribute("userDTO") UserDTO userDTO,
                             Model model) {
        try {
            userDTO.setUserId(userId);
            userService.updateUser(userId, userDTO);
            model.addAttribute("successMessage", "User updated successfully");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "user/edit";
    }

    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable Long userId, Model model) {
        try {
            userService.deleteUser(userId);
            model.addAttribute("successMessage", "User deleted successfully");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "user/index";
    }
}
