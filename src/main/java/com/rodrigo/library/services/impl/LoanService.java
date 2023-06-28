package com.rodrigo.library.services.impl;

import com.rodrigo.library.models.entity.Loan;
import com.rodrigo.library.repository.BookRepository;
import com.rodrigo.library.repository.LoanRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    private LoanRepository repository;
    private BookRepository bookRepository;
    public LoanService(LoanRepository repository){
        this.repository = repository;
    }
    public Loan save(Loan loan) {

        var book = bookRepository.getBookByIsbn(loan.getIsbn());

        if(book == null) throw new EntityNotFoundException("Book not found");

        loan.setBook(book);

        return repository.save(loan);
    }
}
