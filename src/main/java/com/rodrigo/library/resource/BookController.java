package com.rodrigo.library.resource;


import com.rodrigo.library.dto.BookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO createBook(){
       var dados = new BookDTO(1L, "meu livro", "Autor", "123123");

       return dados;
    }
}
