package org.bank.fintech.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Model: Representa a tabela do banco de dados no ambiente JAVA.
//As classes aqui devem ser "burras". Ou seja, apenas dados (private attributes) e formas de acessá-los (Getters/Setters)
//Basicamente construímos uma classe Conta contendo atributos, id, titular, cpf, e etc.
//Os métodos Getters/Setters manipulam os atributos ou seja, mudam os estados deles.


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conta{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private double saldo;

    @Column(nullable=false)
    private LocalDate dataNascimento;

    @NotBlank(message="O nome do titular é obrigatório.")
    @Column(nullable=false, unique=true)
    private String titular;

    @NotBlank(message="O CPF é obrigatório.")
    @Pattern(regexp="\\d{3}\\.\\d{3}\\.\\d{3}\\-\\{2}\\", message="O CPF deve estar no formato 000.000.000.00")
    @Column(nullable=false, unique=true)
    private String cpf;

    @Column(nullable=false, columnDefinition="boolean default true")
    private Boolean ativo = true;

    @OneToMany(mappedBy="conta")
    private List<Transacao> transacao;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    
}