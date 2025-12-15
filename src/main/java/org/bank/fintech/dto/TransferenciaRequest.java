package org.bank.fintech.dto;

public class TransferenciaRequest {
    private Long idOrigem;
    private Long idDestino;
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
