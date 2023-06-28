package com.rodrigo.library.services.impl;

import com.rodrigo.library.exceptions.BusinessException;
import com.rodrigo.library.models.entity.Book;
import com.rodrigo.library.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {


    BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book save(Book book) {

        if (repository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("isbn repetido");
        }
        return repository.save(book);
    }

    public Book findOne(Long id){
        return repository.getReferenceById(id);
    }

    public void delete(Book entity){
        var newEntity = this.findOne(entity.getId());

        if(newEntity == null) throw new EntityNotFoundException("Book not found");

        repository.delete(newEntity);
    }

    public Book update(Book entity){
        var newEntity = this.findOne(entity.getId());

        if(newEntity == null) throw new EntityNotFoundException("Book not found");
        return repository.save(entity);
    }

    public Page<Book> findAll(Pageable paginacao) {
        return repository.findAll(paginacao);
    }
}
