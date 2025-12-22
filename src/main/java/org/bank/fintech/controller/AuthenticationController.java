package org.bank.fintech.controller;

import org.bank.fintech.dto.AuthenticationDTO;
import org.bank.fintech.dto.LoginResponseDTO;
import org.bank.fintech.dto.RegisterDTO;
import org.bank.fintech.infra.security.TokenService;
import org.bank.fintech.model.Usuario;
import org.bank.fintech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.Data;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity <?> login(@RequestBody @Valid AuthenticationDTO data) {
        
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getSenha());
        
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
    

    @PostMapping("/register")
    public ResponseEntity <?> register(@RequestBody RegisterDTO data) {
        
        if (repository.findByLogin(data.getLogin()) != null) return ResponseEntity.badRequest().build();
        
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getSenha());

        Usuario newUSer = new Usuario(null, data.getLogin(), encryptedPassword, data.getRole());
        
        repository.save(newUSer);

        return ResponseEntity.ok().build();
    }
    
}
