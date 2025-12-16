package org.bank.fintech.service;

import java.util.*;
import org.bank.fintech.model.*;
import org.bank.fintech.repository.*;
import org.bank.fintech.dto.*;
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
    
        Conta conta = repository.findById(id).orElseThrow(()-> new RuntimeException("ERRO! Conta não encontrada"));

        if(conta.getAtivo() == false){
            throw new IllegalArgumentException("ERRO! Conta inativa! Operação não realizada!");
        }
            
        Double SaldoAtual = conta.getSaldo();

        conta.setSaldo(SaldoAtual + valor);

        repository.save(conta);

        Transacao historico = new Transacao(valor, TipoTransacao.DEPOSITO, conta);
        
        transacaoRepository.save(historico);
    }

    @Transactional
    public void sacar(Long id, Double valor){

        Conta conta = repository.findById(id).orElseThrow(()-> new RuntimeException("ERRO! Conta não encontrada"));

        if(conta.getAtivo() == false){
            throw new IllegalArgumentException("ERRO! Conta inativa! Operação não realizada!");
        }
        
        if (conta.getSaldo() < valor){
            throw new IllegalArgumentException("ERRO! Saldo insuficiente para realizar saque!");
        }
        Double SaldoAtual = conta.getSaldo();

        conta.setSaldo(SaldoAtual - valor);

        Transacao historico = new Transacao(valor, TipoTransacao.SAQUE, conta);
        
        transacaoRepository.save(historico);

        repository.save(conta);
    }

    public ExtratoResponse consultarExtrato(Long id){
        Conta conta = repository.findById(id).orElseThrow(()-> new RuntimeException("ERRO! Conta não encontrada!"));

        List<Transacao> transacoes = transacaoRepository.findByContaId(id);

        return new ExtratoResponse(conta.getTitular(), conta.getSaldo(), transacoes);
    }

    public Conta criar(Conta conta){
        if (conta.getCpf() == null) {
            throw new IllegalArgumentException("ERRO! CPF obrigatório para criar conta bancária!");
        }

        String cpfLimpo = conta.getCpf().replaceAll("[^0-9]","");
        conta.setCpf(cpfLimpo);

        if (repository.existsByCpf(cpfLimpo)){
            throw new IllegalArgumentException(("ERRO! CPF já está em Uso!"));
        }
        conta.setSaldo(0.0);
        return repository.save(conta);
    }

    public Double consultarSaldo(Long id){

        Conta conta = repository.findById(id).orElseThrow(() -> new RuntimeException("ERRO! Conta não encontrada!"));

        return conta.getSaldo();
    }

    public void apagar(Long id) {
        if (repository.existsById(id)){
            repository.deleteById(id);
        }
        else{
            throw new RuntimeException("ERRO! Não é possível apagar. Conta não encontrada");
        }

    }

    @Transactional
    public void transferir(Long idOrigem, Long idDestino, Double valor){
        

        if (idOrigem.equals(idDestino)){
            throw new IllegalArgumentException("ERRO! Conta de Origem e Destinão não podem ser iguais!");
        }

        Conta origem = repository.findById(idOrigem).orElseThrow(()-> new RuntimeException("ERRO! Conta de Origem não encontrada!"));

        Conta destino = repository.findById(idDestino).orElseThrow(() -> new RuntimeException("ERRO! Conta de Destino não encontrada!"));

        if(origem.getAtivo() == false){
            throw new IllegalArgumentException("ERRO! Conta inativa! Operação não realizada!");
        }
        if(destino.getAtivo() == false){
            throw new IllegalArgumentException("ERRO! Conta inativa! Operação não realizada!");
        }

        if (origem.getSaldo() < valor){
            throw new IllegalArgumentException("ERRO! Saldo insuficiente para transferência!");
        }

        origem.setSaldo(origem.getSaldo() - valor);
        destino.setSaldo(destino.getSaldo() + valor);

        repository.save(origem);
        repository.save(destino);

        Transacao historicoDestino = new Transacao(valor, TipoTransacao.TRANSFERENCIA_RECEBIDA, destino);
    
        transacaoRepository.save(historicoDestino);

        Transacao historicoOrigem = new Transacao(valor, TipoTransacao.TRANSFERENCIA_ENVIADA, origem);
    
        transacaoRepository.save(historicoOrigem);
    
    }

    public void encerrar(Long id){
        Conta conta = repository.findById(id).orElseThrow(()-> new RuntimeException("ERRO! Conta não encontrada!"));

        conta.setAtivo(false);

        repository.save(conta);
    }

    public Conta atualizar(Long id, String novoTitular){
        Conta conta = repository.findById(id).orElseThrow(()-> new RuntimeException("ERRO! Conta não encontrada."));

        if(novoTitular != null && !novoTitular.isBlank()){
            conta.setTitular(novoTitular);
        }
        
        return repository.save(conta);
    }

}
