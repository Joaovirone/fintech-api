package org.bank.fintech.controller;

import org.bank.fintech.dto.AtualizacaoContaRequest;
import org.bank.fintech.dto.CriarContaRequest;
import org.bank.fintech.dto.DepositoRequest;
import org.bank.fintech.dto.ExtratoResponse;
import org.bank.fintech.dto.SaqueRequest;
import org.bank.fintech.dto.TransferenciaRequest;
import org.bank.fintech.model.Conta;
import org.bank.fintech.service.ContaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;






//Controller: Ser a porta de entrada da API REST. Aqui ele recebe requisições HTTP e devolve respostas HTTP.
//Controller não foi feito para as regras de negócio, ele apenas recebe e entrega as requisições.


@RestController
@RequestMapping("/contas")
@Tag(name = "Gestão de contas bancárias!", description = "Endpoints para criação, movimentação e consultas de contas bancárias!")
public class ContaController {
    
    private final ContaService service;

    public ContaController(ContaService service){
        this.service = service;
    }


    @PostMapping
    @Operation(summary="Criar nova conta", description="Cria uma conta com saldo inicial zerado. Requer CPF único.")
    public ResponseEntity<?> criarConta(@RequestBody @Valid CriarContaRequest dados){

            
        
            Conta novaConta = new Conta();
            novaConta.setTitular(dados.getTitular());
            novaConta.setCpf(dados.getCpf());
            novaConta.setDataDeNascimento(dados.getDataNascimento());

            Conta contaCriada = service.criar(novaConta,dados.getValorInicial());

            return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);



    }

    @PostMapping("/{id}/deposito")
    @Operation(summary="Realiza depósito", description="Adiciona valor positivo ao saldo da conta informada.")
    public ResponseEntity<?> depositar(@PathVariable Long id,@RequestBody @Valid DepositoRequest request) {
        
        service.depositar(id,request.getValor());
        return ResponseEntity.ok("OK! Depósito realizado com sucesso!");

    }
    

    @PostMapping("/{id}/saque")
    @Operation(summary="Realiza saque", description="Debita valor do saldo, se houve fundos suficientes.")
    public ResponseEntity<?> sacar(@PathVariable Long id, @RequestBody @Valid SaqueRequest request) {
        
            service.sacar(id, request.getValor());
            return ResponseEntity.ok("Saque realizado com Sucesso!");
       
    }

    @PostMapping("/transferir")
    @Operation(summary="Realiza as transferências", description="Transfere valores entre duas contas existentes. Operação atômica")
    public ResponseEntity<?> transferir(@RequestBody @Valid TransferenciaRequest request){

            service.transferir(request.getIdOrigem(), request.getIdDestino(), request.getValor());
            return ResponseEntity.ok("Transferência realizada com sucesso!");
        
        }
    
    
    @GetMapping("/{id}/extrato")
    @Operation(summary="Consulta o extrato", description="Retorna os dados da conta, saldo atual e lista de transações (saques/depósitos)")
    public ResponseEntity<?> coinsultarExtrato(@PathVariable Long id){
        
            ExtratoResponse extrato = service.consultarExtrato(id);
            return ResponseEntity.ok(extrato);

        
        }
    

    @GetMapping("/{id}/saldo")
    @Operation(summary="Consulta o saldo", description="Retorna apenas o valor número do saldo atual.")
    public ResponseEntity<?> consultarSaldo(@PathVariable Long id){
        
            Double saldo = service.consultarSaldo(id);
            return ResponseEntity.status(HttpStatus.OK).body(saldo);

    }
    @GetMapping
    public ResponseEntity<List<Conta>> listarContas() {

        List<Conta> contas = service.listarTodas();

        return ResponseEntity.ok(contas);
    }
    

    @PutMapping("/{id}")
    @Operation(summary="Atualizar dados", description="Atualiza nome do titular. Não altera saldo")
    public ResponseEntity<?> atualizarConta(@PathVariable Long id, @RequestBody @Valid AtualizacaoContaRequest request) {
        
        Conta contaAtualizada = service.atualizar(id, request.getTitular());
        return ResponseEntity.ok(contaAtualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Encerrar conta", description="Desativa a conta (Soft Delete). O histórico é mantido.")
    public ResponseEntity<?> encerrarConta (@PathVariable Long id){

            service.encerrar(id);
            return ResponseEntity.ok("Conta encerrada com sucesso!");
       
    }
    
}
