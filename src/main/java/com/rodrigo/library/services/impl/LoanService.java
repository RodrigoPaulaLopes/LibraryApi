package com.rodrigo.library.services.impl;

import com.rodrigo.library.exceptions.BookAlreadyLoanedException;
import com.rodrigo.library.exceptions.BusinessException;
import com.rodrigo.library.exceptions.LoanNotFoundException;
import com.rodrigo.library.models.entity.Loan;
import com.rodrigo.library.repository.BookRepository;
import com.rodrigo.library.repository.LoanRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    private LoanRepository repository;

    private BookRepository bookRepository;

    public LoanService(LoanRepository repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    public Loan getByIsbn(String isbn){
        var loaned = repository.findByIsbn(isbn);

        if(loaned == null) throw new LoanNotFoundException("this book has not been borrowed");

       return loaned;
    }
    public Loan save(Loan loan) {

      var book = bookRepository.getBookByIsbn(loan.getIsbn());
      if (book == null) throw new EntityNotFoundException("Book not found.");


        var loaned = repository.findByIsbn(loan.getIsbn());
        System.out.println(loaned);

        if(loaned != null) throw new BookAlreadyLoanedException("Book already loaned");

        loan.setBook(book);

        return repository.save(loan);
    }
}
