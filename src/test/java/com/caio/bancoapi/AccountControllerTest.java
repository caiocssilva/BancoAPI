package com.caio.bancoapi;

import com.caio.bancoapi.controller.AccountController;
import com.caio.bancoapi.entity.Account;
import com.caio.bancoapi.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    private Account mockAccount;

    @BeforeEach
    void setup() {
        mockAccount = new Account("Caio", "Corrente", 1000.0);

        // Simulação da autenticação
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("Caio");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext); // Define o contexto de segurança para o teste
    }

    @Test
    public void testCreateAccount() {
        // Mock do comportamento do serviço
        when(accountService.createAccount(any(Account.class), anyString())).thenReturn(mockAccount);

        // Chama o método no controller
        ResponseEntity<Account> response = accountController.createAccount(mockAccount);

        // Verifica se o método foi chamado corretamente
        verify(accountService, times(1)).createAccount(any(Account.class), anyString());

        // Valida a resposta
        assertEquals(201, response.getStatusCode().value());  // Código HTTP 201 Created
        assertEquals(mockAccount, response.getBody());  // Verifica se o corpo da resposta é o mockAccount
    }


}
