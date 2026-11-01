package com.biblioteca.ProjetoBiblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EsqueciSenhaRequest(
		@NotBlank @Email(message = "informe um e-mail válido") @Size(max = 180) String email) {
}
