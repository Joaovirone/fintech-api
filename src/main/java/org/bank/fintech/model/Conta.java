package org.bank.fintech.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

//Model: Representa a tabela do banco de dados no ambiente JAVA.
//As classes aqui devem ser "burras". Ou seja, apenas dados (private attributes) e formas de acessá-los (Getters/Setters)
//Basicamente construímos uma classe Conta contendo atributos, id, titular, cpf, e etc.
//Os métodos Getters/Setters manipulam os atributos ou seja, mudam os estados deles.

@Entity
public class Conta{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double saldo;
    private Date dataDeNascimento;

    @NotBlank(message="O nome do titular é obrigatório.")
    private String titular;

    @NotBlank(message="O CPF é obrigatório.")
    @Pattern(regexp="\\d{3}\\.\\d{3}\\.\\d{3}\\-\\{2}\\", message="O CPF deve estar no formato 000.000.000.00")
    private String cpf;

    @Column(nullable=false)
    private Boolean ativo = true;

    

    public Conta(){}

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public double getSaldo(){
        return saldo;
    }
    public void setSaldo(double saldo){
        this.saldo = saldo;
    }
    public String getTitular(){
        return titular;
    }
    public void setTitular(String titular){
        this.titular = titular;
    }
    public Date getDataDeNascimento(){
        return dataDeNascimento;
    }

    public void setDataDeNascimento(Date dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }
    public String getCpf(){
        return cpf;
    }
    public void setCpf(String cpf){
        this.cpf = cpf;
    }
    public Boolean getAtivo(){
        return ativo;
    }

    public void setAtivo(Boolean ativo){
        this.ativo = ativo;
    }
}