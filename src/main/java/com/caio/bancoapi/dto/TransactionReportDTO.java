package com.caio.bancoapi.dto;

import lombok.Data;
import java.util.List;

@Data
public class TransactionReportDTO {

    private List<PaymentResponseDTO> transactions;
    private double totalPayments;

    public TransactionReportDTO(List<PaymentResponseDTO> transactions, double totalPayments) {
        this.transactions = transactions;
        this.totalPayments = totalPayments;
    }
}
