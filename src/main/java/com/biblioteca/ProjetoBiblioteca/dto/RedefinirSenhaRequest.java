package com.biblioteca.ProjetoBiblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RedefinirSenhaRequest(
		@NotBlank @Email(message = "informe um e-mail válido") @Size(max = 180) String email,
		@NotBlank @Pattern(regexp = "\\d{6}", message = "o código deve ter 6 dígitos") String codigo,
		@NotBlank @Size(min = 8, max = 72, message = "a senha deve ter de 8 a 72 caracteres") String novaSenha) {
}
