package com.example.ShopShoes.service;

import com.example.ShopShoes.dto.TransactionDTO;
import java.util.List;

public interface TransactionService {

    TransactionDTO getTransactionById(Long id);
    List<TransactionDTO> getAllTransactions();
    List<TransactionDTO> getTransactionsByUserId(Long userId);
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO);
    void deleteTransaction(Long id);
}
