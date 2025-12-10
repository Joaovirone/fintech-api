package controller;


import model.Conta;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ContaService;


@RestController
@RequestMapping
public class ContaController {
    @Autowired
    private ContaService service;


    @PostMapping
    public ResponseEntity<Conta> criarConta(@RequestBody Conta conta){
        try{
            Conta novaConta = service.criar(conta);

            return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(service.criar(conta));
    }
    public ResponseEntity<Conta> apagarConta(@RequestBody Conta conta){
        return ResponseEntity.ok(service.apagar(conta));
    }

    }

