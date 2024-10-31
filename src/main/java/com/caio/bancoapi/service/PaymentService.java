package com.caio.bancoapi.service;

import com.caio.bancoapi.dto.PaymentResponseDTO;
import com.caio.bancoapi.dto.TransactionReportDTO;
import com.caio.bancoapi.entity.Account;
import com.caio.bancoapi.entity.Payment;
import com.caio.bancoapi.repository.AccountRepository;
import com.caio.bancoapi.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

        // Atualiza o saldo da conta e a data do pagamento
        account.setInitialBalance(account.getInitialBalance() - payment.getValue());
        payment.setDate(LocalDateTime.now());
        accountRepository.save(account); // Salva a conta atualizada

        return paymentRepository.save(payment); // Salva o pagamento
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Método para retornar o saldo atualizado da conta
    public double getUpdatedBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        return account.getInitialBalance();
    }

    // Método para gerar relatório de transações por conta e intervalo de datas
    public TransactionReportDTO generateTransactionReport(Long accountId, LocalDate startDate, LocalDate endDate) {
        // Converte as datas para LocalDateTime
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        // Filtra os pagamentos da conta no intervalo de datas
        List<Payment> payments = paymentRepository.findByAccountIdAndDateBetween(accountId, startDateTime, endDateTime);

        // Mapeia para PaymentResponseDTO e calcula a soma total
        List<PaymentResponseDTO> transactionList = payments.stream()
                .map(payment -> new PaymentResponseDTO(
                        payment.getId(),
                        payment.getAccountId(),
                        payment.getValue(),
                        payment.getDate(),
                        payment.getDescription(),
                        getUpdatedBalance(payment.getAccountId())
                ))
                .collect(Collectors.toList());

        double totalPayments = payments.stream().mapToDouble(Payment::getValue).sum();

        return new TransactionReportDTO(transactionList, totalPayments);
    }
}
