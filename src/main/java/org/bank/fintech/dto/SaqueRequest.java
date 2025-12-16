package org.bank.fintech.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SaqueRequest {
    
    @NotNull(message="O valor é obrigatório")
    @Positive(message="O valor deve ser maior que 0 (Zero)")
    private Double valor;


    public Double getValor (){
        return valor;
    }

    public void SetValor (Double valor){
        this.valor = valor;
    }
}
