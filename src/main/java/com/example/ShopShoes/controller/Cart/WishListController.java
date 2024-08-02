package com.example.ShopShoes.controller.Cart;

import com.example.ShopShoes.dto.WishListDTO;
import com.example.ShopShoes.service.WishListService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/wishlist")
@AllArgsConstructor
public class WishListController {

    private final WishListService wishListService;

    @GetMapping
    public String wishlist(Model model, Principal principal) {

        if (principal != null) {
            List<WishListDTO> wishListItems = wishListService.getWishListItemsByUser(principal.getName());
            model.addAttribute("wishListItems", wishListItems);
            return "fe/wishlist";
        }

        return "fe/wishlist";
    }

    @PostMapping("/add-to-wishlist")
    public String addToWishList(@RequestParam Long productId, Principal principal, Model model){
        try {
            wishListService.createWishList(productId, principal.getName());
            model.addAttribute("successWishlist", "Sản phẩm đã được thêm vào danh sách yêu thích");
            return "redirect:/wishlist";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorWishlist", e);
        }
        return "redirect:/wishlist";
    }

    @PostMapping("/remove/{wishListId}")
    public String removeItemFromWishList(@PathVariable Long wishListId) {
        try {
            wishListService.deleteWishList(wishListId);
        } catch (IllegalArgumentException ignored) {
            return "redirect:/wishlist";
        }
        return "redirect:/wishlist";
    }

    @PostMapping("/update/{wishListId}")
    public String updateWishListQuantity(@Valid @ModelAttribute("wishlist") WishListDTO wishListDTO, @PathVariable Long wishListId) {
        try {
            wishListService.updateWishList(wishListId, wishListDTO);
        } catch (IllegalArgumentException ignored) {
            return "redirect:/wishlist";
        }
        return "redirect:/wishlist";
    }
}
