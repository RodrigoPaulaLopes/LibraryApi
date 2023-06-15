package com.rodrigo.library.resource;


import com.rodrigo.library.dto.BookDTO;
import com.rodrigo.library.models.entity.Book;
import com.rodrigo.library.services.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    BookService bookService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO dados, UriComponentsBuilder builder){
        var uri = builder.path("/api/books/{id}").buildAndExpand(dados.id()).toUri();
        var entity = bookService.save(new Book(dados));
       return ResponseEntity.created(uri).body(new BookDTO(entity));
    }
}
