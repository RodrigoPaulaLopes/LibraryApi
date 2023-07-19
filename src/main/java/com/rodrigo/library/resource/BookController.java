package com.rodrigo.library.resource;


import com.rodrigo.library.dto.BookDTO;
import com.rodrigo.library.dto.CreateBookDTO;
import com.rodrigo.library.models.entity.Book;
import com.rodrigo.library.services.impl.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Library")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping
    @Operation(description =  "Realiza a busca de todos os livros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "retorna todos os livros com sucesso")
    })
    public ResponseEntity<Page<BookDTO>> findAll(Pageable paginacao){
        return ResponseEntity.ok().body(bookService.findAll(paginacao).map(BookDTO::new));
    }
    @PostMapping
    @Transactional
    @Operation(description =  "Realiza a criação de um livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "dados invalidos."),
            @ApiResponse(responseCode = "400", description = "Livro com ISBN repetido."),
            @ApiResponse(responseCode = "500", description = "erro no servidor."),

    })
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody CreateBookDTO dados, UriComponentsBuilder builder){
        var entity = bookService.save(new Book(dados));
        var uri = builder.path("/api/books/{id}").buildAndExpand(entity.getId()).toUri();
        return ResponseEntity.created(uri).body(new BookDTO(entity));
    }

    @GetMapping("/{id}")
    @Operation(description =  "Realiza a busca de um livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro criado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado."),
            @ApiResponse(responseCode = "500", description = "erro no servidor."),

    })
    public ResponseEntity<BookDTO> findBook(@PathVariable Long id){
        var entity = bookService.findOne(id);
        return ResponseEntity.ok().body(new BookDTO(entity));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro deletado sucesso."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado."),
            @ApiResponse(responseCode = "500", description = "erro no servidor."),

    })
    @Operation(description =  "Realiza a deleção de um livro")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        var entity = bookService.findOne(id);
        if(entity == null) throw new EntityNotFoundException("Book not found");

        bookService.delete(entity);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    @Operation(description =  "Realiza a alteração de um livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro alterado com sucesso."),
            @ApiResponse(responseCode = "400", description = "dados invalidos."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado."),
            @ApiResponse(responseCode = "500", description = "erro no servidor."),

    })
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
