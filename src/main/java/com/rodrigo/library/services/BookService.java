package com.rodrigo.library.services;

import com.rodrigo.library.models.entity.Book;
import org.springframework.stereotype.Service;


public interface BookService {
    Book save(Book book);
}
