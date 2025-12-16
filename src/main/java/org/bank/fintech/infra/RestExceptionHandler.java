package org.bank.fintech.infra;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.*;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
    

    private ResponseEntity<String> tratarErroRegraDeNegocio(IllegalArgumentException exception){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<String> tratarErroRuntime(RuntimeException exception){
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
