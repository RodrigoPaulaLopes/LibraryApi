package com.rodrigo.library.dto;

import com.rodrigo.library.models.entity.Loan;
import jakarta.validation.constraints.NotBlank;

public record LoanDTO(Long id, @NotBlank String isbn, @NotBlank String constumer) {
    public LoanDTO(Loan salvedLoan) {
        this(salvedLoan.getId(), salvedLoan.getIsbn(), salvedLoan.getConstumer());
    }
}
