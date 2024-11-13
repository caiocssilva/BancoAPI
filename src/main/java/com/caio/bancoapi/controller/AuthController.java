package com.caio.bancoapi.controller;

import com.caio.bancoapi.dto.AuthenticationRequest;
import com.caio.bancoapi.dto.AuthenticationResponse;
import com.caio.bancoapi.dto.UserResponseDTO;
import com.caio.bancoapi.repository.UserRepository;
import com.caio.bancoapi.entity.User;
import com.caio.bancoapi.util.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Usuário já existe");
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        // Salva o usuário no banco de dados
        User createdUser = userRepository.save(user);

        // Converter User para DTO
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(createdUser.getId());
        userResponseDTO.setUsername(createdUser.getUsername());
        userResponseDTO.setRole(createdUser.getRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        // Carregar os detalhes do usuário autenticado
        final User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        final String jwt = jwtUtil.generateToken(user.getUsername());

        // Retornar o token JWT, nome de usuário e role
        return ResponseEntity.ok(new AuthenticationResponse(user.getUsername(), user.getRole(), jwt));
    }
}
