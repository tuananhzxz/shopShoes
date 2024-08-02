package com.example.ShopShoes.controller.Auth;

import com.example.ShopShoes.dto.OrderDTO;
import com.example.ShopShoes.dto.TransactionDTO;
import com.example.ShopShoes.dto.UserDTO;
import com.example.ShopShoes.entity.User;
import com.example.ShopShoes.service.OrderService;
import com.example.ShopShoes.service.TransactionService;
import com.example.ShopShoes.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/myaccount")
@AllArgsConstructor
public class MyAccountController {

        private final TransactionService transactionService;
        private final OrderService orderService;
        private final UserService userService;

        @GetMapping
        public String myaccount(Model model, Principal principal){
            UserDTO userDTO = userService.getUserByUsername(principal.getName());
            Long id = userDTO.getUserId();
            List<OrderDTO> orderDTOList = orderService.getAllOrdersByUserId(id);
            model.addAttribute("orderDTOList", orderDTOList);
            try {
                UserDTO user = userService.updateUser(id, userDTO);
                model.addAttribute("user", user);
                model.addAttribute("successMessage", "Thay đổi thành công");
            } catch (Exception e) {
                model.addAttribute("errorMessage", e.getMessage());
            }
            return "fe/dashboard";
        }

        @GetMapping("/cancelOrder/{orderId}")
        public String cancelOrder(@PathVariable Long orderId, Model model){
            try {
                orderService.cancelOrder(orderId);
                model.addAttribute("successMessage", "Đã hủy đơn hàng");
            } catch (Exception e) {
                model.addAttribute("errorMessage", e.getMessage());
            }
            return "redirect:/myaccount";
        }
}
