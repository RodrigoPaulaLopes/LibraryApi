package com.rodrigo.library.services;

import com.rodrigo.library.dto.BookDTO;
import com.rodrigo.library.exceptions.BusinessException;
import com.rodrigo.library.models.entity.Book;
import com.rodrigo.library.repository.BookRepository;
import com.rodrigo.library.services.impl.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.awt.print.Pageable;
import java.lang.reflect.Array;
import java.util.Arrays;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @DisplayName("Deve retornar todos os livros")
    public void allBooksTest(){
        //cenario
        var books = Arrays.asList(new Book(1L, "Meu Livro", "Rodrigo Lopes", "123123"));
        Mockito.when(repository.findAll()).thenReturn(books);
        //execução
        var booksReturned = repository.findAll();
        //verificação

        Assertions.assertThat(booksReturned).isNotEmpty();
        Assertions.assertThat(booksReturned).hasSize(1);
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

    @Test
    @DisplayName("Deve obter um livro por id")
    public void mustRecieveBookForId() {
        //cenario
        var bookDto = new BookDTO(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        var book = new Book(bookDto);

        Mockito.when(repository.getReferenceById(book.getId())).thenReturn(book);

        //execução

        var foundBook = repository.getReferenceById(book.getId());

        //verificação

        Assertions.assertThat(foundBook).isNotNull();
        Assertions.assertThat(foundBook.getId()).isEqualTo(book.getId());
        Assertions.assertThat(foundBook.getTitle()).isEqualTo(book.getTitle());
        Assertions.assertThat(foundBook.getAuthor()).isEqualTo(book.getAuthor());
        Assertions.assertThat(foundBook.getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Deve retornar um erro quando não encontrar um livro pelo id")
    public void mustRecieveErrorWhenNotFoundBookForId() {
        //cenario
        var id = 1L;

        Mockito.when(repository.getReferenceById(id)).thenThrow(new EntityNotFoundException("Book not found"));

        //execução

        var exception = Assertions.catchThrowable(() -> bookService.findOne(id));

        //verificação

        Assertions.assertThat(exception).isInstanceOf(EntityNotFoundException.class).hasMessage("Book not found");
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void mustDeleteBook() {

        //cenario
        var bookDto = new BookDTO(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        var book = new Book(bookDto);

        Mockito.when(repository.getReferenceById(book.getId())).thenReturn(book);

        //execução
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->  bookService.delete(book));

        //verificação
        Mockito.verify(repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve lançar erro ao deletar um livro")
    public void mustErrorDeleteBook() {

        //cenario
        var bookDto = new BookDTO(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        var book = new Book(bookDto);

        //execução
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.delete(book));

        //verificação
        Mockito.verify(repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve lançar erro ao atualizar um livro")
    public void mustErrorUpdateBook() {

        //cenario
        var bookDto = new BookDTO(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        var book = new Book(bookDto);

        //execução
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.update(book));

        //verificação
        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void mustUpdateBook(){
        //cenario
        var bookDto = new BookDTO(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        var book = new Book(bookDto);

        Mockito.when(repository.getReferenceById(book.getId())).thenReturn(book);
        Mockito.when(repository.save(book)).thenReturn(book);

        //execução

        var updatedBook = bookService.update(book);

        //verificação

        Assertions.assertThat(updatedBook).isNotNull();
        Assertions.assertThat(updatedBook.getId()).isEqualTo(book.getId());
        Assertions.assertThat(updatedBook.getTitle()).isEqualTo(book.getTitle());
        Assertions.assertThat(updatedBook.getAuthor()).isEqualTo(book.getAuthor());
        Assertions.assertThat(updatedBook.getIsbn()).isEqualTo(book.getIsbn());
    }
}
