package com.example.ShopShoes.controller.Order;

import com.example.ShopShoes.dto.TransactionDTO;
import com.example.ShopShoes.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/admin/order/transaction/{id}")
    public TransactionDTO getTransactionDetails(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }
}
