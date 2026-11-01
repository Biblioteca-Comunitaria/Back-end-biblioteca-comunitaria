package com.biblioteca.ProjetoBiblioteca.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.biblioteca.ProjetoBiblioteca.entities.Bibliotecario;
import com.biblioteca.ProjetoBiblioteca.repositories.BibliotecarioRepository;

/** Ensina o Spring Security a buscar o login na tabela de bibliotecários. */
@Service
public class BibliotecarioUserDetailsService implements UserDetailsService {

	private final BibliotecarioRepository bibliotecarioRepository;

	public BibliotecarioUserDetailsService(BibliotecarioRepository bibliotecarioRepository) {
		this.bibliotecarioRepository = bibliotecarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Bibliotecario bibliotecario = bibliotecarioRepository
				.findByUsuario(BibliotecarioService.normalizarUsuario(username))
				.orElseThrow(() -> new UsernameNotFoundException("Bibliotecário não encontrado."));
		return User.withUsername(bibliotecario.getUsuario())
				.password(bibliotecario.getSenha())
				.roles("BIBLIOTECARIO")
				.build();
	}
}
