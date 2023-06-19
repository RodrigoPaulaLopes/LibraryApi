package com.rodrigo.library.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(String isbnIsDuplicated) {
        super(isbnIsDuplicated);
    }
}
