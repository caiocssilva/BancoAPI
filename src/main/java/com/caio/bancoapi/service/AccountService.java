package com.caio.bancoapi.service;

import com.caio.bancoapi.dto.AccountResponseDTO;
import com.caio.bancoapi.entity.Account;
import com.caio.bancoapi.entity.User;
import com.caio.bancoapi.repository.AccountRepository;
import com.caio.bancoapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public Account createAccount(Account account, String username) {
        if (account.getInitialBalance() < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }

        // Busca o usuário pelo nome de usuário
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Associa o usuário autenticado à conta
        account.setUser(user);
        account.setCurrentBalance(account.getInitialBalance());

        return accountRepository.save(account);
    }

    // Método para obter todas as contas
    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(account -> {
                    if (account.getUser() == null) {
                        throw new RuntimeException("Conta com ID " + account.getId() + " não possui usuário associado.");
                    }
                    return new AccountResponseDTO(
                            account.getId(),
                            account.getName(),
                            account.getAccountType(),
                            account.getInitialBalance(), // Saldo inicial
                            calculateCurrentBalance(account) // Saldo atualizado
                    );
                })
                .collect(Collectors.toList());
    }

    // Método para obter uma conta pelo ID com verificação de permissões
    public Account getAccountById(Long id, String username, Collection<? extends GrantedAuthority> authorities) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        Account account = accountOptional.orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        // Verifica se o usuário é o dono da conta ou se ele tem a role ADMIN
        if (account.getUser().getUsername().equals(username) ||
                authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return account;
        } else {
            throw new RuntimeException("Acesso negado: Você não tem permissão para acessar esta conta");
        }
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
