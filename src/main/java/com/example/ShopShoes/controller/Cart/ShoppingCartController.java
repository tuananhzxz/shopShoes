package com.example.ShopShoes.controller.Cart;

import com.example.ShopShoes.dto.CardItemDTO;
import com.example.ShopShoes.dto.ShippingOptionDTO;
import com.example.ShopShoes.entity.ShippingOption;
import com.example.ShopShoes.repository.ShippingOptionRepository;
import com.example.ShopShoes.service.ShippingOptionService;
import com.example.ShopShoes.service.ShoppingCartService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final ShippingOptionService shippingOptionService;

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Long productId, @RequestParam Integer quantity, Model model, Principal principal) {
        shoppingCartService.addItemToCart(productId, quantity, principal.getName());
        model.addAttribute("successMessage", "Sản phẩm đã được thêm vào giỏ hàng");
        return "redirect:/cart";
    }

    @PostMapping("/remove/{cardItemId}")
    public String removeItemFromCart(@PathVariable Long cardItemId) {
        shoppingCartService.removeItemFromCart(cardItemId);
        return "redirect:/cart";
    }

    @PutMapping("/update/{cardItemId}")
    public ResponseEntity<Void> updateCartItemQuantity(@PathVariable Long cardItemId, @RequestParam Integer quantity) {
        shoppingCartService.updateCartItemQuantity(cardItemId, quantity);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public String cart(Model model, Principal principal){
        if (principal != null) {
            List<CardItemDTO> cartItems = shoppingCartService.getCartItemsByUser(principal.getName());
            model.addAttribute("cartItems", cartItems);
            List<ShippingOptionDTO> shippingOptions = shippingOptionService.getAllShippingOptions();
            model.addAttribute("shippingOptions", shippingOptions);
            BigDecimal totalPrice = cartItems.stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            model.addAttribute("totalQuantity", totalPrice);
            return "fe/cart";
        }
        List<ShippingOptionDTO> shippingOptions = shippingOptionService.getAllShippingOptions();
        model.addAttribute("shippingOptions", shippingOptions);
        return "fe/cart";
    }

    @PutMapping("/update-shipping-option")
    public ResponseEntity<Void> updateCartShippingOption(@RequestParam Long shippingOptionId, Principal principal) {
        shoppingCartService.updateCartShippingOptionForUser(principal.getName(), shippingOptionId);
        return ResponseEntity.noContent().build();
    }

}
