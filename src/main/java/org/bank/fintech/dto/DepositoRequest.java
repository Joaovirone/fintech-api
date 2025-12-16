package org.bank.fintech.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DepositoRequest {

    @NotNull(message="O valor é obrigatório.")
    @Positive(message="O valor deve ser positivo")
    Double valor;


public Double getValor(){
    return valor;
}

public void setValor(Double valor){
    this.valor = valor;
}
}