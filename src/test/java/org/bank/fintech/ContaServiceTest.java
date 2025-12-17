package org.bank.fintech;

import java.util.Optional;

import org.bank.fintech.model.Conta;
import org.bank.fintech.repository.ContaRepository;
import org.bank.fintech.repository.TransacaoRepository;
import org.bank.fintech.service.ContaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
    public class ContaServiceTest {

    @Mock
    private ContaRepository repository;

    
    @Mock
    private TransacaoRepository transacaoRepository;

    
    @InjectMocks
    private ContaService service;


    @Test
    public void deveDepositarComSucesso(){

        //1° Cenário 

        Conta contaFake = new Conta();
        contaFake.setSaldo(100.00);
        contaFake.setAtivo(true);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(contaFake));

        service.depositar(1L, 50.00);

        Assertions.assertEquals(150.00, contaFake.getSaldo());

        Mockito.verify(repository, Mockito.times(1)).save(contaFake);
    }

    @Test
    public void naoDeveDepositarEmContaInativa (){

        //2° Cenário

        Conta contaInativa = new Conta();
        contaInativa.setAtivo(false);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(contaInativa));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            
            service.depositar(1L, 50.00);
        });
    }

    @Test
    public void deveTransferirComSucesso (){

        Conta origem = new Conta();
        origem.setId(1L);
        origem.setSaldo(100.00);
        origem.setAtivo(true);

        Conta destino = new Conta();
        destino.setId(2L);
        destino.setSaldo(20.00);
        destino.setAtivo(true);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(origem));
        Mockito.when(repository.findById(2L)).thenReturn(Optional.of(destino));

        service.transferir(1L , 2L , 50.00);

        Assertions.assertEquals(50.00, origem.getSaldo());

        Assertions.assertEquals(70.00, destino.getSaldo());

        Mockito.verify(repository, Mockito.times(1)).save(origem);
        Mockito.verify(repository, Mockito.times(1)).save(destino);
    }

    @Test
    public void naoDeveTransferirComSucesso (){

        Conta origem = new Conta();

        origem.setId(1L);
        origem.setSaldo(100.00);
        origem.setAtivo(true);

        Conta destino = new Conta();

        destino.setId(2L);
        destino.setSaldo(20.00);
        destino.setAtivo(true);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(origem));
        Mockito.when(repository.findById(2L)).thenReturn(Optional.of(destino));

        service.transferir(1L , 2L , 0.00);

        Assertions.assertEquals(50.00, origem.getSaldo());

        Assertions.assertEquals(70.00, destino.getSaldo());

        Mockito.verify(repository, Mockito.times(1)).save(origem);
        Mockito.verify(repository, Mockito.times(1)).save(destino);
    }
    

}
