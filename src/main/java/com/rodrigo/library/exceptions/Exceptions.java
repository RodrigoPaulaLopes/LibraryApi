package com.rodrigo.library.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Exceptions {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodNotValid(MethodArgumentNotValidException ex){
        var error = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(error.stream().map(Validacao::new).toList());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity methodNotValid(BusinessException ex){
        var error = ex.getMessage();
        return ResponseEntity.badRequest().body(new Message(error));
    }

    public record Message(String errors){
    }
    public record Validacao(String campo, String mensagem){
        public Validacao(FieldError campos){
            this(campos.getField(), campos.getDefaultMessage());
        }
    }
}


