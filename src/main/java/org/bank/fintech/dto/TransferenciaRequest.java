package org.bank.fintech.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransferenciaRequest {
    private Long idOrigem;
    private Long idDestino;
    
    @NotNull(message="O valor é obrigatório.")
    @Positive(message="O valor deve ser positivo")
    private Double valor;


    public Long getIdOrigem(){
        return idOrigem;
    }


    public void setIdOrigem( Long idOrigem){
        this.idOrigem = idOrigem;
    }


    public Long getIdDestino(){
        return idDestino;
    }


    public void setIdDestino( Long idDestino){
        this.idDestino = idDestino;
    }


    public Double getValor(){
        return valor;
    }


    public void setValor( Double valor){
        this.valor = valor;
    }
}
