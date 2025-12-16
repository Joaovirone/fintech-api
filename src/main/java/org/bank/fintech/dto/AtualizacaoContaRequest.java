package org.bank.fintech.dto;

import jakarta.validation.constraints.NotBlank;

public class AtualizacaoContaRequest {
    
    @NotBlank(message="O nome do titular é obrigatório")
    private String titular;

    public String getTitular(){
        return titular;
    }
    public void setTitular(String titular){
        this.titular = titular;
    }
}
