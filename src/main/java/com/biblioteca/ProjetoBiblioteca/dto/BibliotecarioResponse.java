package com.biblioteca.ProjetoBiblioteca.dto;

import com.biblioteca.ProjetoBiblioteca.entities.Bibliotecario;

/** Nunca inclui a senha, nem o hash. */
public record BibliotecarioResponse(Long id, String nome, String usuario, String email) {

	public static BibliotecarioResponse de(Bibliotecario b) {
		return new BibliotecarioResponse(b.getId(), b.getNome(), b.getUsuario(), b.getEmail());
	}
}
