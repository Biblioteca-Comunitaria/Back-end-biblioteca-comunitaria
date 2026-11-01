package com.biblioteca.ProjetoBiblioteca.dto;

public record LivroResponse(
		Long id,
		String titulo,
		String autor,
		String isbn,
		int quantidadeExemplares,
		String genero,
		String editora,
		Integer anoPublicacao) {
}
