package com.rodrigo.library.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigo.library.dto.BookDTO;
import com.rodrigo.library.dto.LoanDTO;
import com.rodrigo.library.models.entity.Book;
import com.rodrigo.library.models.entity.Loan;
import com.rodrigo.library.services.impl.BookService;
import com.rodrigo.library.services.impl.LoanService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(LoanController.class)
public class LoanControllerTest {


    public static String LOAN_API = "/api/v1/loans";

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    LoanService loanService;

    @MockBean
    BookService bookService;

    @Test
    @DisplayName("Deve criar um novo emprestimo de um livro")
    public void createLoanBook() throws Exception {
        //cenario
        var loanDto = new LoanDTO(1L, "1234", "Rodrigo lopes");
        var json = mapper.writeValueAsString(loanDto);

        var book = new Book(1L, "Meu Livro", "Rodrigo Lopes", "123123");

        var date = LocalDate.now();
        var loan = new Loan(1L, "1234", "Rodrigo lopes", book, date);

        BDDMockito.given(bookService.getBookByIsbn(loanDto.isbn())).willReturn(book);
        BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);
        //execução

        var request = MockMvcRequestBuilders
                .post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verificação
        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L));
    }
    @Test
    @DisplayName("Deve retornar um erro ao tentar fazer request com dados faltando")
    public void returnBadRequestBook() throws Exception {
        //cenario
        var LoanDto = new LoanDTO(null, "", "");
        var json = mapper.writeValueAsString(LoanDto);

        var request = MockMvcRequestBuilders
                .post(LOAN_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    @DisplayName("Deve retornar um erro ao não encontrar um livro pelo isbn")
    public void returnBookNotFound() throws Exception {
        //cenario
        var loanDto = new LoanDTO(1L, "1234", "Rodrigo lopes");
        var json = mapper.writeValueAsString(loanDto);

        BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willThrow(EntityNotFoundException.class);

        //execução

        var request = MockMvcRequestBuilders
                .post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verificação
        mvc.perform(request)
                .andExpect(status().isNotFound()).andExpect(jsonPath("errors").value("Book not found"));

    }
}
