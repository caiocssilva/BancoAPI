package com.caio.bancoapi.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType tipoConta;

    @Column(nullable = false)
    private BigDecimal saldoInicial;

    public Account() {}

    public Account(String nome, AccountType tipoConta, BigDecimal saldoInicial) {
        this.nome = nome;
        this.tipoConta = tipoConta;
        this.saldoInicial = saldoInicial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public AccountType getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(AccountType tipoConta) {
        this.tipoConta = tipoConta;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }
}