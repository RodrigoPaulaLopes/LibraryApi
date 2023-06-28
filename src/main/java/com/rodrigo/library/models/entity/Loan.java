package com.rodrigo.library.models.entity;

import com.rodrigo.library.dto.LoanDTO;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Loan {


    private Long id;
    private String isbn;
    private String constumer;
    private Book book;
    private LocalDate loanDate;
    private Boolean returned;

    public Loan(long id, String isbn, String costumer, Book book, LocalDate date) {
        this.setId(id);
        this.setIsbn(isbn);
        this.setConstumer(costumer);
        this.setBook(book);
        this.setLoanDate(date);
    }

    public Loan(LoanDTO dto) {
        this.setConstumer(dto.constumer());
        this.setIsbn(dto.isbn());
    }
}
