package com.rodrigo.library.models.entity;

import com.rodrigo.library.dto.BookDTO;
import com.rodrigo.library.dto.CreateBookDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String isbn;


    @OneToMany(mappedBy = "book")
    private List<Loan> loans = new ArrayList<>();

    public Book(BookDTO dados) {
        this.setId(dados.id());
        this.setTitle(dados.title());
        this.setAuthor(dados.author());
        this.setIsbn(dados.isbn());

    }
    public Book(CreateBookDTO dados) {
        this.setTitle(dados.title());
        this.setAuthor(dados.author());
        this.setIsbn(dados.isbn());

    }


    public Book(String title, String author, String isbn) {
        this.setTitle(title);
        this.setAuthor(author);
        this.setIsbn(isbn);

    }
    public Book(Long id, String title, String author, String isbn) {
        this.setId(id);
        this.setTitle(title);
        this.setAuthor(author);
        this.setIsbn(isbn);

    }
    public void updateData(BookDTO dados) {
        this.setTitle(dados.title());
        this.setAuthor(dados.author());
        this.setIsbn(dados.isbn());
    }
}
