package com.rodrigo.library.repository;


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

}
