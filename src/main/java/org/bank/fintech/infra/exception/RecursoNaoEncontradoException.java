package org.bank.fintech.infra.exception;

public class RecursoNaoEncontradoException extends RuntimeException{
    
    public RecursoNaoEncontradoException (String mensagem){
        super(mensagem);
    }
}
