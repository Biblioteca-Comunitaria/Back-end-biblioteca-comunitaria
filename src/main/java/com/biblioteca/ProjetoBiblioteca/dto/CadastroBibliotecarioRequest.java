package com.biblioteca.ProjetoBiblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CadastroBibliotecarioRequest(
		@NotBlank String nome,
		// sem ":" porque o HTTP Basic separa usuário e senha por esse caractere
		@NotBlank @Pattern(regexp = "[A-Za-z0-9._-]{3,50}", message = "use de 3 a 50 letras, números, ponto, hífen ou sublinhado") String usuario,
		// opcional para manter compatibilidade, mas necessário para a recuperação de senha
		@Email(message = "informe um e-mail válido") @Size(max = 180) String email,
		// BCrypt só considera os 72 primeiros bytes da senha
		@NotBlank @Size(min = 8, max = 72, message = "a senha deve ter de 8 a 72 caracteres") String senha) {
}
