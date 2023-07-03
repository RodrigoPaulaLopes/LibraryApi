package com.rodrigo.library.models.entity;

import com.rodrigo.library.dto.LoanDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String isbn;
    @Column
    private String costumer;

    @ManyToOne

    @JoinColumn(name = "book_id")
    private Book book;
    @Column
    private LocalDate loanDate;
    @Column
    private Boolean returned;

    public Loan(long id, String isbn, String costumer, Book book, LocalDate date) {
        this.setId(id);
        this.setIsbn(isbn);
        this.setCostumer(costumer);
        this.setBook(book);
        this.setLoanDate(date);
    }

    public Loan(LoanDTO dto) {
        this.setCostumer(dto.costumer());
        this.setIsbn(dto.isbn());
    }
}
