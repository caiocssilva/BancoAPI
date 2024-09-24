package com.caio.bancoapi.repository;

import com.caio.bancoapi.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByAccountId(Long accountId); // Busca todos os pagamentos de uma conta específica
}
