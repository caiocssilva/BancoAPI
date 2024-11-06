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

        // Log do saldo inicial e valor do pagamento
        System.out.println("Saldo inicial da conta: " + account.getCurrentBalance());
        System.out.println("Valor do pagamento: " + payment.getValue());

        // Verifica se o saldo da conta é suficiente
        if (account.getCurrentBalance() < payment.getValue()) {
            throw new IllegalArgumentException("Saldo insuficiente para cobrir o pagamento.");
        }

        // Define o saldo atual no pagamento antes de atualizar a conta
        payment.setCurrentBalance(account.getCurrentBalance());

        // Atualiza o saldo da conta
        account.setCurrentBalance(account.getCurrentBalance() - payment.getValue());

        // Define a data do pagamento
        payment.setDate(LocalDateTime.now());

        // Log do saldo após subtrair o pagamento
        System.out.println("Saldo após pagamento: " + account.getCurrentBalance());
        System.out.println("Saldo registrado no pagamento: " + payment.getCurrentBalance());

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
        return account.getCurrentBalance();
    }

    // Método para gerar relatório de transações por conta e intervalo de datas
    public TransactionReportDTO generateTransactionReport(Long accountId, LocalDate startDate, LocalDate endDate) {
        // Converte as datas para LocalDateTime
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        // Recupera o saldo inicial da conta
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        // Filtra os pagamentos da conta no intervalo de datas
        List<Payment> payments = paymentRepository.findByAccountIdAndDateBetween(accountId, startDateTime, endDateTime);

        // Mapeia para PaymentResponseDTO e calcula a soma total
        List<PaymentResponseDTO> transactionList = payments.stream()
                .map(payment -> new PaymentResponseDTO(
                            payment.getId(),
                            payment.getAccountId(),
                            payment.getName(),
                            payment.getValue(),
                            payment.getDate(),
                            payment.getDescription(),
                            payment.getCurrentBalance() // Usa o saldo atual armazenado na conta
                ))
                .collect(Collectors.toList());

        double totalPayments = payments.stream().mapToDouble(Payment::getValue).sum();

        return new TransactionReportDTO(transactionList, totalPayments);
    }
}
