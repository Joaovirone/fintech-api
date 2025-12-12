package controller;

import model.Conta;
import dto.DepositoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ContaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



//Controller: Ser a porta de entrada da API REST. Aqui ele recebe requisições HTTP e devolve respostas HTTP.
//Controller não foi feito para as regras de negócio, ele apenas recebe e entrega as requisições.


@RestController
@RequestMapping("/contas")
public class ContaController {
    @Autowired
    private ContaService service;


    @PostMapping
    public ResponseEntity<?> criarConta(@RequestBody Conta conta){
        try{
            Conta novaConta = service.criar(conta);

            return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);

        } catch (IllegalArgumentException e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    
    }

    @PostMapping("/{id}/deposito")
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

    @GetMapping("/{id}/saldo")
    public ResponseEntity<?> consultarSaldo(@PathVariable Long id){
        try{
            Double saldo = service.consultarSaldo(id);
            return ResponseEntity.status(HttpStatus.OK).body(saldo);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagarConta(@PathVariable Long id){
        try{
            service.apagar(id);
            return ResponseEntity.status(HttpStatus.OK).body(apagarConta(id));
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    }

