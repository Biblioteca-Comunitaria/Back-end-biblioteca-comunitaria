package com.biblioteca.ProjetoBiblioteca.dto;

import jakarta.validation.constraints.NotNull;

public record EmprestimoRequest(
		@NotNull Long livroId,
		@NotNull Long leitorId) {
}
