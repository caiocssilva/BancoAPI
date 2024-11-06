package com.caio.bancoapi.controller;

import com.caio.bancoapi.service.AccountService;
import com.caio.bancoapi.dto.PaymentResponseDTO;
import com.caio.bancoapi.dto.TransactionReportDTO;
import com.caio.bancoapi.entity.Payment;
import com.caio.bancoapi.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody Payment payment) {
        Payment createdPayment = paymentService.createPayment(payment);

        // Extrai o nome do titular da conta e o saldo atualizado
        String accountHolder = accountService.getAccountById(createdPayment.getAccountId()).getName();
        double currentBalance = paymentService.getUpdatedBalance((createdPayment.getAccountId()));

        // Constrói o DTO para a resposta
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(
                createdPayment.getId(),
                createdPayment.getAccountId(),
                accountHolder,
                createdPayment.getValue(),
                createdPayment.getDate(),
                createdPayment.getDescription(),
                currentBalance
        );

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Retorna uma lista de todos os pagamentos no sistema
    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();

        // Mapeia cada pagamento para o DTO
        List<PaymentResponseDTO> responseDTOs = payments.stream()
                .map(payment -> {
                    String accountHolder = accountService.getAccountById(payment.getAccountId()).getName();
                    double currentBalance = paymentService.getUpdatedBalance(payment.getAccountId());
                    return new PaymentResponseDTO(
                            payment.getId(),
                            payment.getAccountId(),
                            accountHolder,
                            payment.getValue(),
                            payment.getDate(),
                            payment.getDescription(),
                            paymentService.getUpdatedBalance(payment.getAccountId())
                    );
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Endpoint para gerar relatório de transações por conta e o intervalo de datas
    @GetMapping("/report")
    public ResponseEntity<TransactionReportDTO> generateTransactionReport(
            @RequestParam Long accountId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        // Converte as strings de data para LocalDate
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // Gera o relatório de transações
        TransactionReportDTO report = paymentService.generateTransactionReport(accountId, start, end);

        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
