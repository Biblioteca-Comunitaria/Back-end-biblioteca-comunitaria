package com.biblioteca.ProjetoBiblioteca.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastroLivroRequest(
		@NotBlank String titulo,
		@NotBlank String autor,
		@NotBlank String isbn,
		@NotNull @Min(1) Integer quantidadeExemplares,
		@Size(max = 120) String genero,
		@Size(max = 120) String editora,
		@Min(1000) @Max(3000) Integer anoPublicacao) {
}
