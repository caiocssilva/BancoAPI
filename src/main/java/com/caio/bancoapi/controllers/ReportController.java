package com.caio.bancoapi.controller;

import com.caio.bancoapi.dto.TransactionReport;
import com.caio.bancoapi.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionReport>> getTransactionReport(
            @RequestParam Long accountId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<TransactionReport> report = reportService.getTransactionReport(accountId, startDate, endDate);
        return ResponseEntity.ok(report);
    }
}
