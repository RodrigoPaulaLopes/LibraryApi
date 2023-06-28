package com.rodrigo.library.repository;


import com.rodrigo.library.dto.BookDTO;
import com.rodrigo.library.models.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;


    @Test()
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base de dados")
    public void returnTrueIfBookAlreadyExistsInDatabase() {
        //cenario

        var isbn = "123456";
        var book = new Book( "Meu Livro 2", "amanda santos", isbn);
        entityManager.persistAndFlush(book);

        //execução
        var exists = bookRepository.existsByIsbn(isbn);


        //verificação
        Assertions.assertThat(exists).isTrue();
    }

    @Test()
    @DisplayName("Deve retornar falso quando existir um livro na base de dados")
    public void returnFalseIfBookAlreadyExistsInDatabase() {
        //cenario

        var isbn = "123457";

        //execução

        var exists = bookRepository.existsByIsbn(isbn);


        //verificação

        Assertions.assertThat(exists).isFalse();
    }


    @Test
    @DisplayName("Deve salvar um livro na base de dados")
    public void saveBookTest(){
        //cenario
        var book = new Book(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        //execução

        var savedBook = bookRepository.save(book);

        //verificação

        Assertions.assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest(){
        //cenario
        //criei um livro e persisti ele na base de dados
        var book = new Book("Meu Livro 2", "claudio soarez", "12456");
        entityManager.persist(book);
        //execução

        //procurei o livro salvo na base de dados
        var foundBook = entityManager.find(Book.class, book.getId());
        //deletei ele com o metodo delete do repository
        bookRepository.delete(foundBook);

        //procurei novamente esse livro
        var deletedBook = entityManager.find(Book.class, book.getId());

        //verificação
        //agora verifico se ele é null
        Assertions.assertThat(deletedBook).isNull();
    }
}
