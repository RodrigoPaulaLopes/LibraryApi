package com.rodrigo.library.resource;

import com.rodrigo.library.dto.LoanDTO;
import com.rodrigo.library.models.entity.Loan;
import com.rodrigo.library.services.impl.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {
    private LoanService loanService;

    public LoanController(LoanService loanService){
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanDTO> create(@Valid @RequestBody LoanDTO dto, UriComponentsBuilder builder){
        var loan = new Loan(dto);
        var salvedLoan = loanService.save(loan);
        var uri = builder.buildAndExpand("/api/v1/loans/{id}").expand(salvedLoan.getId()).toUri();

        return ResponseEntity.created(uri).body(new LoanDTO(salvedLoan));
    }
    

}
