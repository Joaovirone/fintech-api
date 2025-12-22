package org.bank.fintech.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class CriarContaRequest {
    
    @NotBlank (message = "O nome do titular é obrigatório.")
   private String titular;

    @NotBlank (message = "Número do CPF é obrigatório.")
    private String cpf;

    @NotNull (message = "A data de nascimento é obrigatória.")
    private LocalDate dataNascimento;

    private Double valorInicial = 0.0;
    
}
