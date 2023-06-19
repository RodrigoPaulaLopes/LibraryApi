package com.rodrigo.library.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Exceptions {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodNotValid(MethodArgumentNotValidException ex){
        var error = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(error.stream().map(Validacao::new).toList());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity entityNotFound(EntityNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("Book not found"));

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


