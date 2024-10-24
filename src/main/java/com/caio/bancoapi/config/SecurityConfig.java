package com.caio.bancoapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Desabilita a proteção CSRF para testes
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/accounts/create", "/api/accounts", "/api/accounts/**").permitAll() // Permite acesso à rota de criação de contas
                        .anyRequest().authenticated() // Requer autenticação para outras requisições
                );
        return http.build();
    }
}
