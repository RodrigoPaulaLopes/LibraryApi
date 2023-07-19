package com.rodrigo.library;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "library", description = "Api do projeto de emprestimos de livros.",version = "1.0", contact = @Contact(name = "Rodrigo de paula lopes", email = "rodrigo.plopesti@gmail.com", url = "https://github.com/RodrigoPaulaLopes")))
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}
