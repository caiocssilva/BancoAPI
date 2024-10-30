package com.caio.bancoapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentResponseDTO {
    private Long id;
    private Long accountId;
    private double value;
    private LocalDateTime date;
    private String description;
    private double updatedBalance;

}
