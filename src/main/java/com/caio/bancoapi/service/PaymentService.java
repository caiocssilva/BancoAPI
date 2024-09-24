package com.caio.bancoapi.service;

import com.caio.bancoapi.entity.Payment;
import com.caio.bancoapi.entity.Account;
import com.caio.bancoapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Payment createPayment(Payment payment) {
        Account account = accountRepository.findById(payment.getAccountId())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        // Verifica se a conta tem saldo suficiente
        if (account.getBalance() < payment.getAmount()) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        // Atualiza o saldo da conta
        account.setBalance(account.getBalance() - payment.getAmount());
        accountRepository.save(account);

        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByAccount(Long accountId) {
        return paymentRepository.findByAccountId(accountId);
    }
}
