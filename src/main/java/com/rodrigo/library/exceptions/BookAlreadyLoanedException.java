package com.rodrigo.library.exceptions;

public class BookAlreadyLoanedException extends RuntimeException{
    public BookAlreadyLoanedException(String message){
        super(message);
    }
}
