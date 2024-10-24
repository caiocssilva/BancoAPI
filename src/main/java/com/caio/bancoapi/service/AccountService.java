package com.caio.bancoapi.service;

import com.caio.bancoapi.entity.Account;
import com.caio.bancoapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        if (account.getInitialBalance() < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        return accountRepository.save(account);
    }

    // Método para obter todas as contas
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Método para obter uma conta pelo ID
    public Account getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.orElseThrow(() -> new RuntimeException("Conta não encontrada"));
    }

    // Método para atualizar uma conta
    public Account updateAccount(Long id, Account account) {
        // Verifica se a conta existe
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Conta não encontrada");
        }
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
}
