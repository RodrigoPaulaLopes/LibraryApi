package com.rodrigo.library.repository;

import com.rodrigo.library.models.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Loan findByIsbn(String isbn);
}
