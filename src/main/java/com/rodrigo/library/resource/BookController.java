package com.rodrigo.library.resource;


import com.rodrigo.library.dto.BookDTO;
import com.rodrigo.library.dto.CreateBookDTO;
import com.rodrigo.library.models.entity.Book;
import com.rodrigo.library.services.impl.BookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping
    public ResponseEntity<Page<BookDTO>> findAll(Pageable paginacao){
        return ResponseEntity.ok().body(bookService.findAll(paginacao).map(BookDTO::new));
    }
    @PostMapping
    @Transactional
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody CreateBookDTO dados, UriComponentsBuilder builder){
        var entity = bookService.save(new Book(dados));
        var uri = builder.path("/api/books/{id}").buildAndExpand(entity.getId()).toUri();
        return ResponseEntity.created(uri).body(new BookDTO(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> createBook(@PathVariable Long id){
        var entity = bookService.findOne(id);
        return ResponseEntity.ok().body(new BookDTO(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        var entity = bookService.findOne(id);
        if(entity == null) throw new EntityNotFoundException("Book not found");

        bookService.delete(entity);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<BookDTO> updateBook(@RequestBody BookDTO dados){

        //buscar o livro na base de dados
        var entity = bookService.findOne(dados.id());

        //verificar se o livro existe
        if(entity == null) {
            throw new EntityNotFoundException("Book not found");
        }

        //se o livro existir passar os dados vindo da requisição para atualizar o objeto atual
        entity.setAuthor(dados.author());
        entity.setTitle(dados.title());
        entity.setIsbn(dados.isbn());

        //alterar os dados no banco de dados e receber o objeto atualizado
        var newEntity = bookService.update(entity);
        return ResponseEntity.ok().body(new BookDTO(newEntity));
    }
}
