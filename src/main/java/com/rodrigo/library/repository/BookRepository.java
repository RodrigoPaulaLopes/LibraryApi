package com.rodrigo.library.repository;

import com.rodrigo.library.models.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);

    Book getBookByIsbn(String isbn);
}
