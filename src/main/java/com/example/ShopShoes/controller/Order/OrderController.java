package com.example.ShopShoes.controller.Order;

import com.example.ShopShoes.dto.OrderDTO;
import com.example.ShopShoes.dto.TransactionDTO;
import com.example.ShopShoes.service.OrderService;
import com.example.ShopShoes.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/order")
public class OrderController {

    private final OrderService orderService;
    private final TransactionService transactionService;

    @GetMapping
    public String order(Model model,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDTO> ordersPage;

        if (keyword != null && !keyword.isEmpty()) {
            ordersPage = orderService.searchOrderByStatus(keyword, pageable);
        } else {
            ordersPage = orderService.getAllOrder(pageable);
        }

        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("keyword", keyword);
        return "order/index";
    }

    @GetMapping("/edit/{orderId}")
    public String editOrder(@PathVariable Long orderId, Model model) {
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        List<String> statusList = Arrays.asList("Chờ xác nhận", "Đang giao hàng", "Hủy", "Thành công");
        List<String> paymentMethods = Arrays.asList("Chuyển khoản trực tiếp", "Hóa đơn thanh toán", "Paypal", "Thẻ tín dụng");
        model.addAttribute("paymentMethods", paymentMethods);
        model.addAttribute("statusList", statusList);
        model.addAttribute("orderDTO", orderDTO);
        return "order/edit";
    }

    @PostMapping("/edit/{orderId}")
    public String updateOrder(@PathVariable Long orderId,
                              @ModelAttribute("orderDTO") OrderDTO orderDTO,
                              Model model) {
        try {
            orderDTO.setOrderId(orderId);
            orderService.updateOrder(orderId, orderDTO);
            model.addAttribute("successMessage", "Order updated successfully");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating order: " + e.getMessage());
            return "order/edit";
        }
        return "redirect:/admin/order";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") Long id, Model model) {
        try {
            orderService.deleteOrder(id);
            model.addAttribute("successMessage", "Order deleted successfully");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error deleting order: " + e.getMessage());
            return "redirect:/admin/order";
        }
        return "redirect:/admin/order";
    }
}
