package com.rodrigo.library.services;

import com.rodrigo.library.dto.BookDTO;
import com.rodrigo.library.exceptions.BusinessException;
import com.rodrigo.library.models.entity.Book;
import com.rodrigo.library.repository.BookRepository;
import com.rodrigo.library.services.impl.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService bookService;
    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.bookService = new BookService(repository);
    }


    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void saveBookTest() {
        //cenario
        var book = new Book(1L, "Meu Livro", "Rodrigo Lopes", "123123");

        Mockito.when(repository.save(book)).thenReturn(new Book(1L, "Meu Livro", "Rodrigo Lopes", "123123"));
        //execução
        var savedBook = bookService.save(book);
        //verificação

        Assertions.assertThat(savedBook.getId()).isNotNull();
        Assertions.assertThat(savedBook.getId()).isEqualTo(1L);
        Assertions.assertThat(savedBook.getTitle()).isEqualTo("Meu Livro");
        Assertions.assertThat(savedBook.getAuthor()).isEqualTo("Rodrigo Lopes");
        Assertions.assertThat(savedBook.getIsbn()).isEqualTo("123123");
    }

    @Test
    @DisplayName("Deve lançar uma exceção quando criar um novo livro com o mesmo isbn")
    public void createBookWithDuplicatedIsbn() {
        //cenario
        var bookDto = new BookDTO(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        var book = new Book(bookDto);
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
        //execução
        var exception = Assertions.catchThrowable(() -> bookService.save(book));

        //verificação
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage("isbn repetido");

        Mockito.verify(repository, Mockito.never()).save(book);
    }


}
