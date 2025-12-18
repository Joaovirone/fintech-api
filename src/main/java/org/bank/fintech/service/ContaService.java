package org.bank.fintech.service;

import java.util.List;

import org.bank.fintech.dto.ExtratoResponse;
import org.bank.fintech.model.Conta;
import org.bank.fintech.model.TipoTransacao;
import org.bank.fintech.model.Transacao;
import org.bank.fintech.repository.ContaRepository;
import org.bank.fintech.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

//Service: o Coração do Sistema, aqui a gente valida, calcula e aplica as regras de negócio.
//O Service não sabe o que é HTTP (WEB) e não sabe gerar SQL.

@Slf4j
@Service
public class ContaService {
     
    private final ContaRepository repository;

    private final TransacaoRepository transacaoRepository;

    public ContaService(ContaRepository repository , TransacaoRepository transacaoRepository){

        this.repository = repository;
        this.transacaoRepository = transacaoRepository; 

    }

    @Transactional
    public void depositar (Long id,Double valor){
    
        log.info("Iniciando método de DEPÓSITO de R$ {} na conta ID: {}", valor, id);

        Conta conta = repository.findById(id).orElseThrow(()-> new RuntimeException("ERRO! Conta não encontrada"));

        if(conta.getAtivo() == false){
            throw new IllegalArgumentException("ERRO! Conta inativa! Operação não realizada!");

        }
            
        Double SaldoAtual = conta.getSaldo();

        conta.setSaldo(SaldoAtual + valor);

        repository.save(conta);

        Transacao historico = new Transacao(valor, TipoTransacao.DEPOSITO, conta);
        
        transacaoRepository.save(historico);

        log.info("Método DEPÓSITO realizado com sucesso na conta ID: {}", id);
    }

    @Transactional
    public void sacar(Long id, Double valor){

        log.info("Iniciando método de SAQUE no valor de {} na conta ID: {}", valor, id);

        Conta conta = repository.findById(id).orElseThrow(()-> new RuntimeException("ERRO! Conta não encontrada"));

        if(conta.getAtivo() == false){
            log.warn("Método de SAQUE não foi realizado devida a CONTA ID |{}| estar inativa", id);
            throw new IllegalArgumentException("ERRO! Conta inativa! Operação não realizada!");
        }
        
        if (conta.getSaldo() < valor){
            log.warn("Conta ID: {} não possui saldo {} para essa operação", id, valor);
            throw new IllegalArgumentException("ERRO! Saldo insuficiente para realizar saque!");
        }
        Double SaldoAtual = conta.getSaldo();

        conta.setSaldo(SaldoAtual - valor);

        Transacao historico = new Transacao(valor, TipoTransacao.SAQUE, conta);
        
        transacaoRepository.save(historico);

        repository.save(conta);

        log.info("Método de SAQUE realizado com sucesso na conta ID: {}", id);
    }

    public ExtratoResponse consultarExtrato(Long id){

        log.info("Iniciando método de CONSULTAR EXTRATO na conta ID: {}", id);

        Conta conta = repository.findById(id).orElseThrow(()-> new RuntimeException("ERRO! Conta não encontrada!"));

        List<Transacao> transacoes = transacaoRepository.findByContaId(id);

        log.info("Método de CONSULTAR EXTRATO foi realizado com sucesso na conta ID: {}", id);

        return new ExtratoResponse(conta.getTitular(), conta.getSaldo(), transacoes);

        
    }

    public Conta criar(Conta conta, Double valorInicial){

        log.info("Iniciando método de CRIAÇÃO DE CONTA para novo CPF: {}", conta.getCpf());

        if (conta.getCpf() == null) {
            throw new IllegalArgumentException("ERRO! CPF obrigatório para criar conta bancária!");
        }

        String cpfLimpo = conta.getCpf().replaceAll("[^0-9]","");
        conta.setCpf(cpfLimpo);

        if (repository.existsByCpf(cpfLimpo)){
            log.warn("Tentativa de criar conta com CPF duplicado: {}", cpfLimpo);
            throw new IllegalArgumentException(("ERRO! CPF já está em Uso!"));
            
        }

        

        conta.setId(null);
        conta.setSaldo(0.0);
        conta.setAtivo(true);

        Conta contaSalva = repository.save(conta);

        if (valorInicial != null && valorInicial > 0 ){

            Transacao historico = new Transacao(valorInicial, TipoTransacao.DEPOSITO, contaSalva);

            transacaoRepository.save(historico);

            contaSalva.setSaldo(valorInicial);

            repository.save(contaSalva);
        }

        log.info("Método de CRIAÇÃO DE CONTA realizado com sucesso.");

        return contaSalva;

        
    }

    public Double consultarSaldo(Long id){

        log.info("Iniciando método de CONSULTAR SALDO na conta ID: {}",id);

        Conta conta = repository.findById(id).orElseThrow(() -> new RuntimeException("ERRO! Conta não encontrada!"));

        log.info("Método de CONSULTAR SALDO foi realizado com sucesso na conta ID: {}",id);
        return conta.getSaldo();
        
    }

    public void apagar(Long id) {

        log.info("Iniciando método de APAGAR CONTA na conta ID: {}",id);

        if (repository.existsById(id)){
            repository.deleteById(id);
        }
        else{
            log.warn("Não foi possível completar a operação pois a conta ID: {} não foi encontrada", id);
            throw new RuntimeException("ERRO! Não é possível apagar. Conta não encontrada");
        }
        log.info("Método de APAGAR CONTA foi realizado com sucesso na conta ID: {}",id);
    }

    @Transactional
    public void transferir(Long idOrigem, Long idDestino, Double valor){

        log.info("Iniciando método de TRANSFERÊNCIA da conta ID ORIGEM |{}| para conta ID DESTINO |{}| no valor de R${}",idOrigem, idDestino, valor);
        

        if (idOrigem.equals(idDestino)){
            log.warn("Método de TRANSFERÊNCIA não realizado pois os IDs de origem |{}| e destino |{}| não podem ser iguais.");
            throw new IllegalArgumentException("ERRO! Conta de Origem e Destinão não podem ser iguais!");
        }

        Conta origem = repository.findById(idOrigem).orElseThrow(()-> new RuntimeException("ERRO! Conta de Origem não encontrada!"));
        Conta destino = repository.findById(idDestino).orElseThrow(() -> new RuntimeException("ERRO! Conta de Destino não encontrada!"));

        if(origem.getAtivo() == false){
            log.warn("Método de TRANSFERÊNCIA não relizado devido a Conta ID ORIGEM |{}| estar inativa.", idOrigem);
            throw new IllegalArgumentException("ERRO! Conta inativa! Operação não realizada!");
        }
        if(destino.getAtivo() == false){
            log.warn("Método de TRANSFERÊNCIA não relizado devido a Conta ID DESTINO |{}| estar inativa.", idDestino);
            throw new IllegalArgumentException("ERRO! Conta inativa! Operação não realizada!");
        }

        if (origem.getSaldo() < valor){
            log.warn("Método de TRANSFERÊNCIA não relizado devido a saldo ser insuficiente R${} .", valor);
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
        
        log.info("Método de TRANSFERÊNCIA foi realizado com sucesso da conta ID ORIGEM |{}| para conta ID DESTINO |{}| no valor de R${}",idOrigem, idDestino, valor);
    }

    public void encerrar(Long id){

        log.info("Iniciando método de ENCERRAR na conta ID: {}", id);
        Conta conta = repository.findById(id).orElseThrow(()-> new RuntimeException("ERRO! Conta não encontrada!"));

        conta.setAtivo(false);
        repository.save(conta);
        log.info("Método de ENCERRAR foi realizado com sucesso na conta ID: {}", id);
    }

    public Conta atualizar(Long id, String novoTitular){

        log.info("Iniciando método de ATUALIZAR CONTA na conta ID: {}", id);

        Conta conta = repository.findById(id).orElseThrow(()-> new RuntimeException("ERRO! Conta não encontrada."));

        if(novoTitular != null && !novoTitular.isBlank()){
            conta.setTitular(novoTitular);
        }

        log.info("Método de ATUALIZAR CONTA  foi realizado com sucesso na conta ID: {}", id);
        return repository.save(conta);

        
    }

}
