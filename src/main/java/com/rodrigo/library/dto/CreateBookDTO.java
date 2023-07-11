package com.rodrigo.library.dto;

import com.rodrigo.library.models.entity.Book;
import jakarta.validation.constraints.NotBlank;

public record CreateBookDTO(@NotBlank String title, @NotBlank String author, @NotBlank String isbn ){
    public CreateBookDTO(Book entity) {
        this(entity.getTitle(), entity.getAuthor(), entity.getIsbn());
    }
}
