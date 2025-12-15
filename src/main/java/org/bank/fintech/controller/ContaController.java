package org.bank.fintech.controller;

import io.swagger.v3.oas.annotations.tags.*;
import io.swagger.v3.oas.annotations.Operation.*;
import org.bank.fintech.dto.DepositoRequest;
import org.bank.fintech.dto.ExtratoResponse;
import org.bank.fintech.dto.SaqueRequest;
import org.bank.fintech.dto.TransferenciaRequest;
import org.bank.fintech.model.Conta;
import org.bank.fintech.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;




//Controller: Ser a porta de entrada da API REST. Aqui ele recebe requisições HTTP e devolve respostas HTTP.
//Controller não foi feito para as regras de negócio, ele apenas recebe e entrega as requisições.


@RestController
@RequestMapping("/contas")
@Tag(name = "Gestão de contas bancárias!", description = "Endpoints para criação, movimentação e consultas de contas bancárias!")
public class ContaController {
    @Autowired
    private ContaService service;


    @PostMapping
    @Operation(summary="Criar nova conta", description="Cria uma conta com saldo inicial zerado. Requer CPF único.")
    public ResponseEntity<?> criarConta(@RequestBody Conta conta){
        try{
            Conta novaConta = service.criar(conta);

            return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);

        } catch (IllegalArgumentException e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    
    }

    @PostMapping("/{id}/deposito")
    @Operation(summary="Realiza depósito", description="Adiciona valor positivo ao saldo da conta informada.")
    public ResponseEntity<?> depositar(@PathVariable Long id,@RequestBody DepositoRequest request) {
        try{
        service.depositar(id,request.getValor());
        
        return ResponseEntity.ok("OK! Depósito realizado com sucesso!");

        } catch (IllegalArgumentException e){
            
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }catch (RuntimeException e){
            
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    }

    @PostMapping("/{id}/saque")
    @Operation(summary="Realiza saque", description="Debita valor do saldo, se houve fundos suficientes.")
    public ResponseEntity<?> sacar(@PathVariable Long id, @RequestBody SaqueRequest request) {
        
        try{
            service.sacar(id, request.getValor());
            return ResponseEntity.ok("Saque realizado com Sucesso!");

        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        
       
    }
    
    @GetMapping("/{id}/extrato")
    @Operation(summary="Consulta o extrato", description="Retorna os dados da conta, saldo atual e lista de transações (saques/depósitos)")
    public ResponseEntity<?> coinsultarExtrato(@PathVariable Long id){
        try{
            ExtratoResponse extrato = service.consultarExtrato(id);

            return ResponseEntity.ok(extrato);

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/saldo")
    @Operation(summary="Consulta o saldo", description="Retorna apenas o valor número do saldo atual.")
    public ResponseEntity<?> consultarSaldo(@PathVariable Long id){
        try{
            Double saldo = service.consultarSaldo(id);
            return ResponseEntity.status(HttpStatus.OK).body(saldo);

        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } 
    }

    @PostMapping("/transferir")
    @Operation(summary="Realiza as transferências", description="Transfere valores entre duas contas existentes. Operação atômica")
    public ResponseEntity<?> transferir(@RequestBody TransferenciaRequest request){
        try{
            service.transferir(request.getIdOrigem(), request.getIdDestino(), request.getValor());

            return ResponseEntity.ok("Transferência realizada com sucesso!");
        } catch (IllegalArgumentException e ){

            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    }

