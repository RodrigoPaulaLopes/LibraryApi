package com.rodrigo.library.services.impl;

import com.rodrigo.library.models.entity.Loan;
import com.rodrigo.library.repository.BookRepository;
import com.rodrigo.library.repository.LoanRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoanService {

    private LoanRepository repository;

    private BookRepository bookRepository;

    public LoanService(LoanRepository repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    public Loan save(Loan loan) {

        var book = bookRepository.getBookByIsbn(loan.getIsbn());

        if (book == null) throw new EntityNotFoundException("Book not found.");

        loan.setBook(book);

        return repository.save(loan);
    }
}
