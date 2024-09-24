package com.caio.bancoapi.service;

import com.caio.bancoapi.entity.Account;
import com.caio.bancoapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        // Validações e regras de negócio
        if (account.getBalance() < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo");
        }
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Conta não encontrada"));
    }

    public Account updateAccount(Long id, Account updatedAccount) {
        // Verifica se a conta existe
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        // Atualiza os dados da conta
        account.setName(updatedAccount.getName());
        account.setAccountType(updatedAccount.getAccountType());
        account.setBalance(updatedAccount.getBalance());

        // Salva as alterações
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        // Verifica se a conta existe antes de deletar
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        accountRepository.delete(account);
    }
}
