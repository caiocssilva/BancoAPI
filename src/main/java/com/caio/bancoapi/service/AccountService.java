package com.caio.bancoapi.service;

import com.caio.bancoapi.entity.Account;
import com.caio.bancoapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
