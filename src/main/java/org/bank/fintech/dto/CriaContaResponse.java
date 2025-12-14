package org.bank.fintech.dto;

 class CriaContaResponse {
    
    private String nome;
    private String cpf;
    private String numeroDaConta;
    private Double saldo;


    public String getNome() {
    return nome;
}

public void setNome(String nome){
    this.nome = nome;
}

public String getCpf() {
    return cpf;
}

public void setCpf(String cpf){
    this.cpf = cpf;
}

public String getNumeroDaConta() {
    return numeroDaConta;
}

public void setNumeroDaCOnta(String numeroDaConta){
    this.numeroDaConta = numeroDaConta;
}

public Double getSaldo() {
    return saldo;
}

public void setCpf(Double saldo){
    this.saldo = saldo;
}
}
