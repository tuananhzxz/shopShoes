package com.example.ShopShoes.service.Impl;

import com.example.ShopShoes.dto.CardItemDTO;
import com.example.ShopShoes.entity.CardItem;
import com.example.ShopShoes.entity.Product;
import com.example.ShopShoes.entity.ShippingOption;
import com.example.ShopShoes.entity.User;
import com.example.ShopShoes.repository.CardItemRepository;
import com.example.ShopShoes.repository.ProductRepository;
import com.example.ShopShoes.repository.ShippingOptionRepository;
import com.example.ShopShoes.repository.UserRepository;
import com.example.ShopShoes.service.ShoppingCartService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ProductRepository productRepository;
    private final CardItemRepository cardItemRepository;
    private final UserRepository userRepository;
    private final ShippingOptionRepository shippingOptionRepository;

    @Override
    public CardItemDTO addItemToCart(Long productId, Integer quantity, String username) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        User user = userRepository.findByUsername(username);
        ShippingOption defaultShippingOption = shippingOptionRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Default shipping option not found with ID: 1"));

        CardItem cardItem = new CardItem();
        cardItem.setProduct(product);
        cardItem.setQuantity(quantity);
        cardItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        cardItem.setUser(user);
        cardItem.setShippingOption(defaultShippingOption);
        cardItem = cardItemRepository.save(cardItem);

        return new CardItemDTO(cardItem);
    }

    @Override
    public void removeItemFromCart(Long cardItemId) {
        cardItemRepository.deleteById(cardItemId);
    }

    @Override
    public void updateCartItemQuantity(Long cardItemId, Integer quantity) {
        CardItem cardItem = cardItemRepository.findById(cardItemId)
                .orElseThrow(() -> new IllegalArgumentException("CardItem not found with ID: " + cardItemId));

        cardItem.setQuantity(quantity);
        cardItem.setTotalPrice(cardItem.getProduct().getPrice().multiply(BigDecimal.valueOf(quantity)));

        cardItemRepository.save(cardItem);
    }

    @Override
    public List<CardItemDTO> getCartItemsByUser(String username) {
        List<CardItem> cardItems = cardItemRepository.findCardItemByUserName(username);
        return cardItems.stream().map(CardItemDTO::new).collect(Collectors.toList());
    }

    @Override
    public void updateCartShippingOptionForUser(String username, Long shippingOptionId) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }

        ShippingOption shippingOption = shippingOptionRepository.findById(shippingOptionId)
                .orElseThrow(() -> new IllegalArgumentException("ShippingOption not found with ID: " + shippingOptionId));

        List<CardItem> cardItems = cardItemRepository.findCardItemByUserName(username);

        for (CardItem cardItem : cardItems) {
            cardItem.setShippingOption(shippingOption);
        }

        cardItemRepository.saveAll(cardItems);
    }

    @Override
    public CardItemDTO getCardItemById(Long id) {
        CardItem cardItem = cardItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CardItem not found with ID: " + id));
        return new CardItemDTO(cardItem);
    }

    @Override
    public void clearCart(String username) {
        List<CardItem> cardItems = cardItemRepository.findCardItemByUserName(username);
        cardItemRepository.deleteAll(cardItems);
    }

}
