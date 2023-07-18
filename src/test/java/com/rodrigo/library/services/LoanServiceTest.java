package com.rodrigo.library.services;

import com.rodrigo.library.exceptions.LoanNotFoundException;
import com.rodrigo.library.models.entity.Book;
import com.rodrigo.library.models.entity.Loan;
import com.rodrigo.library.repository.BookRepository;
import com.rodrigo.library.repository.LoanRepository;
import com.rodrigo.library.services.impl.BookService;
import com.rodrigo.library.services.impl.LoanService;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {
    @MockBean
    BookService bookService;
    @MockBean
    BookRepository bookRepository;
    @MockBean
    LoanRepository loanRepository;

    LoanService loanService;


    @BeforeEach
    public void setUp() {

        this.bookService = new BookService(bookRepository);
        this.loanService = new LoanService(loanRepository, bookRepository);
    }

    @Test
    @DisplayName("Deve retornar um emprestimo pelo isbn do livro")
    public void mustReturnBookWithIsbn() {
        //cenario
        var book = new Book(1L, "Meu livro", "rodrigo lopes", "123");
        var loan = new Loan(1L, book.getIsbn(), "camille", book, LocalDate.now());

        Mockito.when(loanRepository.findByIsbn(book.getIsbn())).thenReturn(loan);

        //execução
        var loanReturned = loanRepository.findByIsbn(book.getIsbn());

        //verificação

        Assertions.assertThat(loanReturned.getId()).isNotNull();
        Assertions.assertThat(loanReturned.getId()).isEqualTo(1L);
        Assertions.assertThat(loanReturned.getIsbn()).isEqualTo("123");
        Assertions.assertThat(loanReturned.getCostumer()).isEqualTo("camille");
        Assertions.assertThat(loanReturned.getBook()).isEqualTo(book);
        Assertions.assertThat(loanReturned.getLoanDate()).isEqualTo(LocalDate.now());


    }

    @Test
    @DisplayName("Deve retornar um erro ao tentar buscar um emprestimo pelo isbn do livro")
    public void mustReturnErrorWithIsbn() {
        //cenario
        var isbn = "123";
        Mockito.when(loanRepository.findByIsbn(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        //execução
        var loanReturned = Assertions.catchThrowable(() -> loanRepository.findByIsbn(isbn));

        //verificação

        Assertions.assertThat(loanReturned).isInstanceOf(EntityNotFoundException.class).hasMessage("this book has not been borrowed");
    }

    @Test
    @DisplayName("Deve salvar um emprestimo")
    public void createLoanTest() {
        //Cenario
        var book = new Book(1L, "Meu livro", "rodrigo lopes", "123");
        var loan = new Loan(1L, book.getIsbn(), "camille", book, LocalDate.now());


        Mockito.when(bookRepository.getBookByIsbn("123")).thenReturn(book);
        Mockito.when(loanRepository.save(loan)).thenReturn(loan);

        //execução
        var loaned = loanService.save(loan);
        //verificação

        Assertions.assertThat(loaned).isNotNull();
        Assertions.assertThat(loaned.getId()).isNotNull();
        Assertions.assertThat(loaned.getId()).isEqualTo(1L);
        Assertions.assertThat(loaned.getIsbn()).isEqualTo("123");
        Assertions.assertThat(loaned.getCostumer()).isEqualTo("camille");
        Assertions.assertThat(loaned.getBook()).isEqualTo(book);
        Assertions.assertThat(loaned.getLoanDate()).isEqualTo(LocalDate.now());

    }

    @Test
    @DisplayName("Deve retornar erro quando um livro não for encontrado")
    public void bookNotFound(){

        var book = new Book(1L, "Meu livro", "rodrigo lopes", "123");
        var loan = new Loan(1L, book.getIsbn(), "camille", book, LocalDate.now());

        Mockito.when(bookRepository.getBookByIsbn(book.getIsbn())).thenThrow(EntityNotFoundException.class);

        Mockito.when(loanRepository.save(loan)).thenThrow(EntityNotFoundException.class);

        var loaned = Assertions.catchThrowable(() -> loanService.save(loan));


        Assertions.assertThat(loaned).isInstanceOf(EntityNotFoundException.class);
        Mockito.verify(loanRepository, Mockito.never()).save(loan);

    }


}
