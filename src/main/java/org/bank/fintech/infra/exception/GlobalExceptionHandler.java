package org.bank.fintech.infra.exception;

import java.time.LocalDateTime;

import org.bank.fintech.dto.DadosErro;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity <DadosErro> HandlerIllegalArgument(IllegalArgumentException e, HttpServletRequest request){

        DadosErro erro = new DadosErro(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Recurso não encontrado",
            e.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity <DadosErro> handleGeneral(Exception e, HttpServletRequest request){

        DadosErro erro = new DadosErro(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro interno no servidor",
            "Ocorreu um erro inesperado. Contate o suporte ",
            request.getRequestURI()
        );

        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }



}
