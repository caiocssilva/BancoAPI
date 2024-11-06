package com.caio.bancoapi.service;

import com.caio.bancoapi.dto.AccountResponseDTO;
import com.caio.bancoapi.entity.Account;
import com.caio.bancoapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        if (account.getInitialBalance() < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        account.setCurrentBalance(account.getInitialBalance());
        return accountRepository.save(account);
    }

    // Método para obter todas as contas
    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(account -> new AccountResponseDTO(
                        account.getId(),
                        account.getName(),
                        account.getAccountType(),
                        account.getInitialBalance(), // Saldo inicial
                        calculateCurrentBalance(account) // Saldo atualizado
                ))
                .collect(Collectors.toList());
    }

    // Método para obter uma conta pelo ID
    public Account getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.orElseThrow(() -> new RuntimeException("Conta não encontrada"));
    }

    // Método para atualizar uma conta
    public Account updateAccount(Long id, Account account) {
        // Verifica se a conta existe e carrega os dados atuais
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        // Manter o saldo inicial da conta existente e atualizar apenas o saldo atual e o nome
        account.setInitialBalance(existingAccount.getInitialBalance());

        // Define o ID da conta que está sendo atualizada
        account.setId(id);
        return accountRepository.save(account);
    }

    // Método para excluir uma conta
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Conta não encontrada");
        }
        accountRepository.deleteById(id);
    }

    private double calculateCurrentBalance(Account account) {
        double totalPayments = account.getPayments().stream()
                .mapToDouble(payment -> payment.getValue())
                .sum();
        return account.getInitialBalance() - totalPayments;
    }
}
