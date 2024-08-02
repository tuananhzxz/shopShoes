package com.example.ShopShoes.service.Impl;

import com.example.ShopShoes.dto.TransactionDTO;
import com.example.ShopShoes.entity.Transaction;
import com.example.ShopShoes.repository.TransactionRepository;
import com.example.ShopShoes.service.TransactionService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionImpl implements TransactionService {

    private final ModelMapper modelMapper;
    private final TransactionRepository transactionRepository;

    @Override
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionDTO.class);
    }

    @Override
    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        existingTransaction.setStatus(transactionDTO.getStatus());
        existingTransaction.setFirstName(transactionDTO.getFirstName());
        existingTransaction.setLastName(transactionDTO.getLastName());
        existingTransaction.setGuestAddress(transactionDTO.getGuestEmail());
        existingTransaction.setGuestPhone(transactionDTO.getGuestPhone());
        existingTransaction.setGuestAddress(transactionDTO.getGuestAddress());
        existingTransaction.setCity(transactionDTO.getCity());
        existingTransaction.setCompany(transactionDTO.getCompany());
        existingTransaction.setZipCode(transactionDTO.getZipCode());
        existingTransaction.setMessage(transactionDTO.getMessage());
        existingTransaction.setAmount(transactionDTO.getAmount());
        existingTransaction.setPayment(transactionDTO.getPayment());
        existingTransaction.setCreated(transactionDTO.getCreated());
        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return modelMapper.map(updatedTransaction, TransactionDTO.class);
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public List<TransactionDTO> getTransactionsByUserId(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        return transactions.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }
}
