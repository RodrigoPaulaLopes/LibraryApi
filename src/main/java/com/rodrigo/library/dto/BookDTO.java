package com.rodrigo.library.dto;

import com.rodrigo.library.models.entity.Book;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
public record BookDTO( Long id, @NotBlank String title, @NotBlank String author, @NotBlank String isbn) {
    public BookDTO(Book entity) {
        this(entity.getId(), entity.getTitle(), entity.getAuthor(), entity.getIsbn());
    }


}
