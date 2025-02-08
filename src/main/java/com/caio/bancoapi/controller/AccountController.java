package com.caio.bancoapi.controller;

import com.caio.bancoapi.dto.AccountResponseDTO;
import com.caio.bancoapi.entity.Account;
import com.caio.bancoapi.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {

        // Recupera o nome de usuário do contexto de autenticação
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Chama o serviço para criar a conta associada ao usuário autenticado
        Account createdAccount = accountService.createAccount(account, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        // Recupera o nome do usuário do contexto de autenticação
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Verifica se o usuário é um ADMIN
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // Se for ADMIN, retorna todas as contas
            List<AccountResponseDTO> accounts = accountService.getAllAccounts();
            return ResponseEntity.ok(accounts);
        } else {
            // Se for um USER, retorna apenas suas próprias contas
            List<AccountResponseDTO> accounts = accountService.getAllAccounts();
            return ResponseEntity.ok(accounts);
        }
    }

    // Método para obter uma conta pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        // Recupera o nome do usuário do contexto de autenticação
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Chama o serviço para obter a conta, passando o nome de usuário e permissões
        try {
            Account account = accountService.getAccountById(id, username, authorities);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    // Método para atualizar uma conta
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        Account updatedAccount = accountService.updateAccount(id, account);
        return ResponseEntity.ok(updatedAccount);
    }

    // Método para excluir uma conta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}