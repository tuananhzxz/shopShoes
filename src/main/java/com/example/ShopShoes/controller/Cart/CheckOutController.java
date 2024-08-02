package com.example.ShopShoes.controller.Cart;

import com.example.ShopShoes.dto.CardItemDTO;
import com.example.ShopShoes.dto.OrderDTO;
import com.example.ShopShoes.dto.TransactionDTO;
import com.example.ShopShoes.service.*;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
@AllArgsConstructor
public class CheckOutController {
    private static final Logger logger = LoggerFactory.getLogger(CheckOutController.class);

    private ShoppingCartService shoppingCartService;
    private OrderService orderService;
    private TransactionService transactionService;
    private PaypalService paypalService;
    private CouponService couponService;

    @GetMapping
    public String checkOut(Model model, Principal principal) {
        model.addAttribute("transaction", new TransactionDTO());
        List<CardItemDTO> cartItems = shoppingCartService.getCartItemsByUser(principal.getName());
        if (cartItems.isEmpty()) {
            logger.warn("Cart is empty for user: " + principal.getName());
            model.addAttribute("errorMessage", "Giỏ hàng rỗng.");
            return "fe/checkout";
        }
        BigDecimal totalPrice = cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalPrice", totalPrice);
        return "fe/checkout";
    }

    @PostMapping
    public String processTransaction(@Valid @ModelAttribute("transaction") TransactionDTO transactionDTO,
                                     Principal principal, Model model, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("Validation errors while submitting form: " + result.getAllErrors());
            return "fe/checkout";
        }

        try {
            List<CardItemDTO> cartItems = shoppingCartService.getCartItemsByUser(principal.getName());
            if (cartItems.isEmpty()) {
                logger.warn("Cart is empty for user: " + principal.getName());
                model.addAttribute("errorMessage", "Giỏ hàng rỗng.");
                return "fe/checkout";
            }

            BigDecimal totalPrice = cartItems.stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (transactionDTO.getCouponCode() != null && !transactionDTO.getCouponCode().isEmpty()) {
                try {
                    BigDecimal discount = couponService.calculateValue(transactionDTO.getCouponCode(), totalPrice);
                    totalPrice = totalPrice.subtract(discount);
                } catch (RuntimeException e) {
                    model.addAttribute("errorMessage", "Mã giảm giá không hợp lệ hoặc không hoạt động.");
                    return "fe/checkout";
                }
            }
            TransactionDTO savedTransaction = createTransaction(transactionDTO, cartItems, totalPrice);
            OrderDTO orderDTO = createOrder(transactionDTO, savedTransaction, cartItems);
            orderService.createOrder(orderDTO);
            shoppingCartService.clearCart(principal.getName());

            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("successMessage", "Đặt hàng thành công");
        } catch (Exception e) {
            logger.error("Error while creating transaction and order: ", e);
            model.addAttribute("errorMessage", "Đặt hàng thất bại");
        }
        return "fe/checkout";
    }


    @PostMapping("/paypal-payment")
    public String initiatePaypalPayment(@Valid @ModelAttribute("transaction") TransactionDTO transactionDTO,
                                        Principal principal, Model model, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("Validation errors while submitting form: " + result.getAllErrors());
            return "fe/checkout";
        }

        try {
            List<CardItemDTO> cartItems = shoppingCartService.getCartItemsByUser(principal.getName());
            if (cartItems.isEmpty()) {
                logger.warn("Cart is empty for user: " + principal.getName());
                model.addAttribute("errorMessage", "Giỏ hàng rỗng.");
                return "fe/checkout";
            }

            BigDecimal totalPrice = cartItems.stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (transactionDTO.getCouponCode() != null) {
                BigDecimal discountedPrice = couponService.calculateValue(transactionDTO.getCouponCode(), totalPrice);
                transactionDTO.setAmount(discountedPrice);
            }

            return "redirect:/";
        } catch (Exception e) {
            logger.error("Error while creating transaction and initiating PayPal payment: ", e);
            model.addAttribute("errorMessage", "Đặt hàng thất bại");
            return "fe/checkout";
        }
    }

    private TransactionDTO createTransaction(TransactionDTO transactionDTO, List<CardItemDTO> cartItems, BigDecimal totalPrice) {
        Long userId = cartItems.get(0).getUserId();
        transactionDTO.setUserId(userId);
        transactionDTO.setAmount(totalPrice);
        return transactionService.createTransaction(transactionDTO);
    }

    private OrderDTO createOrder(TransactionDTO transactionDTO, TransactionDTO savedTransaction, List<CardItemDTO> cartItems) {
        OrderDTO orderDTO = new OrderDTO();
        String productName = cartItems.stream()
                .map(CardItemDTO::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        orderDTO.setProductName(productName);

        String quantity = cartItems.stream()
                .map(item -> item.getQuantity().toString())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        orderDTO.setQuantity(quantity);

        orderDTO.setUserId(savedTransaction.getUserId());
        orderDTO.setTransactionId(savedTransaction.getId());
        orderDTO.setTotalAmount(savedTransaction.getAmount());
        orderDTO.setAddress(transactionDTO.getGuestAddress());
        orderDTO.setPaymentMethod(transactionDTO.getPayment());
        orderDTO.setMessage(transactionDTO.getMessage());
        return orderDTO;
    }

    @PostMapping("/applyCoupon")
    public ResponseEntity<?> applyCoupon(@RequestBody Map<String, String> requestBody, Principal principal) {
        String couponCode = requestBody.get("couponCode");
        try {
            List<CardItemDTO> cartItems = shoppingCartService.getCartItemsByUser(principal.getName());
            if (cartItems.isEmpty()) {
                return ResponseEntity.badRequest().body("Giỏ hàng rỗng.");
            }

            BigDecimal totalPrice = cartItems.stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal discountedPrice = couponService.calculateValue(couponCode, totalPrice);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("discount", discountedPrice);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Áp dụng mã giảm giá thất bại: " + e.getMessage());
        }
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> data, Principal principal) {
        try {
            List<CardItemDTO> cartItems = shoppingCartService.getCartItemsByUser(principal.getName());
            if (cartItems.isEmpty()) {
                return ResponseEntity.badRequest().body("Giỏ hàng rỗng.");
            }

            BigDecimal totalPrice = cartItems.stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (data.containsKey("couponCode") && data.get("couponCode") != null) {
                BigDecimal discount = couponService.calculateValue((String) data.get("couponCode"), totalPrice);
                totalPrice = totalPrice.subtract(discount);
            }

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(totalPrice.multiply(BigDecimal.valueOf(100)).longValue())
                    .setCurrency("usd")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating payment intent: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể tạo ý định thanh toán");
        }
    }
}
