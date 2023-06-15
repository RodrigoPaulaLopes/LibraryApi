package com.rodrigo.library.services.impl;

import com.rodrigo.library.models.entity.Book;
import com.rodrigo.library.repository.BookRepository;
import com.rodrigo.library.services.BookService;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {


    BookRepository repository;

    public BookServiceImpl(BookRepository repository){
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        return repository.save(book);
    }
}
