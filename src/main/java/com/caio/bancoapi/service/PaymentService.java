package com.caio.bancoapi.service;

import com.caio.bancoapi.entity.Account;
import com.caio.bancoapi.entity.Payment;
import com.caio.bancoapi.repository.AccountRepository;
import com.caio.bancoapi.repository.PaymentRepository;
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
        // Verifica se a conta associada ao pagamento existe
        Account account = accountRepository.findById(payment.getAccountId())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        // Verifica se o saldo da conta é suficiente
        if (account.getInitialBalance() < payment.getValue()) {
            throw new IllegalArgumentException("Saldo insuficiente para cobrir o pagamento.");
        }

        // Atualiza o saldo da conta
        account.setInitialBalance(account.getInitialBalance() - payment.getValue());
        accountRepository.save(account); // Salva a conta atualizada

        return paymentRepository.save(payment); // Salva o pagamento
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
