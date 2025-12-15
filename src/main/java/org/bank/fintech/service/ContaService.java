package org.bank.fintech.service;

import org.bank.fintech.model.*;
import org.bank.fintech.repository.ContaRepository;
import org.bank.fintech.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Service: o Coração do Sistema, aqui a gente valida, calcula e aplica as regras de negócio.
//O Service não sabe o que é HTTP (WEB) e não sabe gerar SQL.


@Service
public class ContaService {
    @Autowired
    private ContaRepository repository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Transactional
    public void depositar (Long id,Double valor){
        if (valor <= 0){
            throw new IllegalArgumentException("ERRO! O valor do depósito deve ser positivo!");
        }
    

    Conta conta = repository.findById(id).orElseThrow(()-> new RuntimeException("Conta não encontrada"));
        
    Double SaldoAtual = conta.getSaldo();

    conta.setSaldo(SaldoAtual + valor);

    repository.save(conta);

    Transacao historico = new Transacao(valor, TipoTransacao.DEPOSITO, conta);
    
    transacaoRepository.save(historico);
    }

    public Conta sacar(Long id, Double valor){
        if (valor <= 0){
            throw new IllegalArgumentException("ERRO! O valor do saque deve ser positivo!");
        }
        Conta conta = repository.findById(id).orElseThrow(()-> new RuntimeException("Conta não encontrada"));
        
        if (conta.getSaldo() < valor){
            throw new IllegalArgumentException("ERRO! Saldo insuficiente para realizar saque!");
        }
    Double SaldoAtual = conta.getSaldo();

    conta.setSaldo(SaldoAtual - valor);

    return repository.save(conta);
    }

    public Conta criar(Conta conta){
        if (conta.getCpf() == null) {
            throw new IllegalArgumentException("CPF obrigatório para criar conta bancária!");
        }

        String cpfLimpo = conta.getCpf().replaceAll("[^0-9]","");
        conta.setCpf(cpfLimpo);

        if (repository.existsByCpf(cpfLimpo)){
            throw new IllegalArgumentException(("Erro! CPF já está em Uso!"));
        }
        conta.setSaldo(0.0);
        return repository.save(conta);
    }

    public Double consultarSaldo(Long id){

        Conta conta = repository.findById(id).orElseThrow(() -> new RuntimeException("Conta não encontrada!"));

        return conta.getSaldo();
    }

    public void apagar(Long id) {
        if (repository.existsById(id)){
            repository.deleteById(id);
        }
        else{
            throw new RuntimeException("Não é possível apagar. Conta não encontrada");
        }

    }

    @Transactional
    public void transferir(Long idOrigem, Long idDestino, Double valor){
        
        if (valor <= 0){
            throw new IllegalArgumentException("ERRO! O valor do saque deve ser positivo!");
        }

        if (idOrigem.equals(idDestino)){
            throw new IllegalArgumentException("ERRO! Conta de Origem e Destinão não podem ser iguais!");
        }

        Conta origem = repository.findById(idOrigem).orElseThrow(()-> new RuntimeException("Conta de Origem não encontrada!"));

        Conta destino = repository.findById(idDestino).orElseThrow(() -> new RuntimeException("Conta de Destino não encontrada!"));

        if (origem.getSaldo() < valor){
            throw new IllegalArgumentException("ERRO! Saldo insuficiente para transferência!");
        }

        origem.setSaldo(origem.getSaldo() - valor);
        destino.setSaldo(destino.getSaldo() + valor);

        repository.save(origem);
        repository.save(destino);
    
    }

}
