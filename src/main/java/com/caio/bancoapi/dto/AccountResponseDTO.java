package com.caio.bancoapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountResponseDTO {

    private Long id;
    private String accountHolder;
    private String accountType;
    private Double initialBalance;  // Saldo inicial (valor de criação da conta)
    private Double currentBalance;
}
