package com.rodrigo.library.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigo.library.dto.BookDTO;
import com.rodrigo.library.exceptions.BusinessException;
import com.rodrigo.library.models.entity.Book;
import com.rodrigo.library.services.impl.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(BookControllerTest.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/api/v1/books";
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookService bookService;

    @Test
    @DisplayName("Deve retornar todos os livros da base de dados")
    public void allBooksTest() throws Exception {
        //cenario
        var booksDto = new BookDTO(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        var books = new Book(booksDto);
        BDDMockito.given(bookService.findAll(Mockito.any(Pageable.class))).willReturn(new PageImpl(Arrays.asList(books), PageRequest.of(0, 100), 1));

        //execução
        String queryString = "?page=0&size=100";
        var request = MockMvcRequestBuilders.get(BOOK_API.concat(queryString)).accept(MediaType.APPLICATION_JSON);

        //verificação

        mvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("content").isNotEmpty()).andExpect(jsonPath("content", hasSize(1)));
    }

    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void createBookTest() throws Exception {

        var dto = new BookDTO(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        var book = new Book(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(book);

        //transformando os dados em json
        var json = mapper.writeValueAsString(dto);

        //criando o request
        var request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("id").value(dto.id()))
                .andExpect(jsonPath("title").value(dto.title()))
                .andExpect(jsonPath("author").value(dto.author()))
                .andExpect(jsonPath("isbn").value(dto.isbn()));

    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação de um livro")
    public void createInvalidBookTest() throws Exception {
        var bookdto = new BookDTO(null, "", "", "");
        var json = mapper.writeValueAsString(bookdto);

        var request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DisplayName("Deve lançar erro quando cadastrar novamente o mesmo isbn")
    public void createBookWithDuplicatedIsbn() throws Exception {
        var dto = new BookDTO(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        var json = mapper.writeValueAsString(dto);
        BDDMockito.given(bookService.save(Mockito.any(Book.class))).willThrow(new BusinessException("isbn repetido"));

        var request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("errors").value("isbn repetido"));
    }


    @Test
    @DisplayName("Deve retornar um livro buscado")
    public void returnBook() throws Exception {
        //cenario
        var book = new Book(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        var dto = new BookDTO(book);
        BDDMockito.given(bookService.findOne(book.getId())).willReturn(book);

        var json = mapper.writeValueAsString(dto);

        //execução

        var request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + dto.id()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(json);

        //verificação

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("title").value("Meu Livro"))
                .andExpect(jsonPath("author").value("Rodrigo Lopes"))
                .andExpect(jsonPath("isbn").value("123123"));

    }

    @Test
    @DisplayName("Deve retornar um erro por não encontrar um livro buscado")
    public void returnNotFoundBook() throws Exception {
        //cenario

        BDDMockito.given(bookService.findOne(Mockito.anyLong())).willThrow(EntityNotFoundException.class);

        //execução
        var request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + 1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //verificação

        mvc.perform(request)
                .andExpect(status().isNotFound()).andExpect(jsonPath("errors").value("Book not found"));

    }

    @Test
    @DisplayName("Deve excluir um livro")
    public void mustDeleteBook() throws Exception {
        //cenario
        var book = new Book(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        BDDMockito.given(bookService.findOne(Mockito.anyLong())).willReturn(book);

        //execução

        var request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + book.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);


        //verificação

        mvc.perform(request).andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Deve retornar um erro ao excluir um livro")
    public void mustreturnErrorWhenDeleteBookThatNotExists() throws Exception {
        //cenario
        BDDMockito.given(bookService.findOne(Mockito.anyLong())).willThrow(new EntityNotFoundException("Book not found"));

        //execução

        var request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);


        //verificação

        mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("errors").value("Book not found"));

    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void mustUpdateBook() throws Exception {
        //cenario
        var book = new Book(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        var dto = new BookDTO(book);

        BDDMockito.given(bookService.findOne(1L)).willReturn(book);

        BDDMockito.given(bookService.update(book)).willReturn(book);
        //execução

        var request = MockMvcRequestBuilders
                .put(BOOK_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto));


        //verificação

        mvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("title").value("Meu Livro"))
                .andExpect(jsonPath("author").value("Rodrigo Lopes"))
                .andExpect(jsonPath("isbn").value("123123"));;

    }

    @Test
    @DisplayName("Deve retornar um erro ao atualizar um livro não existente")
    public void mustreturnErrorWhenUpdateBookThatNotExists() throws Exception {
        //cenario
        var book = new Book(1L, "Meu Livro", "Rodrigo Lopes", "123123");
        var dto = new BookDTO(book);
        BDDMockito.given(bookService.findOne(dto.id())).willThrow(new EntityNotFoundException("Book not found"));

        //execução

        var request = MockMvcRequestBuilders
                .put(BOOK_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto));


        //verificação

        mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("errors").value("Book not found"));

    }
}
