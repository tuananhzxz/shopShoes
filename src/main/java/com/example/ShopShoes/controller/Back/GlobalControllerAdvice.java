package com.example.ShopShoes.controller.Back;

import com.example.ShopShoes.controller.Cart.WishListController;
import com.example.ShopShoes.dto.*;
import com.example.ShopShoes.entity.Review;
import com.example.ShopShoes.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
@AllArgsConstructor
public class GlobalControllerAdvice {

    private final ShoppingCartService shoppingCartService;
    private final WishListService wishListService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final ReviewService reviewService;

    @ModelAttribute
    public void addCartItemsToModel(Model model, Principal principal) {
        if (principal != null) {
            List<CardItemDTO> cartItems = shoppingCartService.getCartItemsByUser(principal.getName());
            model.addAttribute("cartItems", cartItems);

            if (cartItems.size() > 4) {
                model.addAttribute("cartItems", cartItems.subList(0, 4));
            }

            model.addAttribute("cartItemCount", cartItems.size());
            BigDecimal totalPrice = cartItems.stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            model.addAttribute("totalQuantity", totalPrice);

            model.addAttribute("cartItemCount", cartItems.size());

            List<WishListDTO> wishList = wishListService.getWishListItemsByUser(principal.getName());
            model.addAttribute("totalWishList", wishList.size());
        }

    }

    @ModelAttribute
    public void transactionDTO(Principal principal, Model model) {
        if (principal != null) {
            UserDTO user = userService.getUserByUsername(principal.getName());
            List<TransactionDTO> transactionDTOList = transactionService.getTransactionsByUserId(user.getUserId());
            TransactionDTO transactionDTO = transactionDTOList.isEmpty() ? new TransactionDTO() : transactionDTOList.get(0);
            model.addAttribute("transactionDTO", transactionDTO);
        }
    }

    @ModelAttribute
    public void listReview(Model model) {
        List<ReviewDTO> reviewList = reviewService.getAllReviews();
        model.addAttribute("reviewList", reviewList);
    }

}
