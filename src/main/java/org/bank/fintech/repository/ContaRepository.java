package org.bank.fintech.repository;

import org.bank.fintech.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repository: faz a ponte entre o JAVA e o banco de dados (SQL)
//Deve ser uma interface e não uma classe.
//Uma interface basicamente define um método ou vários métodos que podem ser usados por quaisquer classes.
//Ao usar o "extends" (estender) JpaRepository, você já ganha métodos prontos: save(), delete(), findAll()

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    boolean existsByCpf(String cpf);
}
