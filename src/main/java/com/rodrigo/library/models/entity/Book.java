package com.rodrigo.library.models.entity;

import com.rodrigo.library.dto.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private Long id;
    private String title;
    private String author;
    private String isbn;

    public Book(BookDTO dados) {
        this.setId(dados.id());
        this.setTitle(dados.title());
        this.setAuthor(dados.author());
        this.setIsbn(dados.isbn());

    }
}
