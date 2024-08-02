package com.example.ShopShoes.controller.Back;

import com.example.ShopShoes.dto.OrderDTO;
import com.example.ShopShoes.entity.Order;
import com.example.ShopShoes.repository.UserRepository;
import com.example.ShopShoes.service.OrderService;
import com.example.ShopShoes.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private OrderService orderService;
    private UserRepository userRepository;
    private ProductService productService;

    @GetMapping
    public String index(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/admin/";
        }

        return "redirect:/logon";
    }

    @RequestMapping("/")
    public String admin(Model model) {
        model.addAttribute("orders", orderService.getAllOrders().size());

        model.addAttribute("products", productService.getAllProducts().size());

        model.addAttribute("users", userRepository.findAll().size());

        model.addAttribute("listOrder", orderService.getOrderRecently());

        model.addAttribute("listProduct", productService.getProductRecently());
        return "admin/index";
    }

}
