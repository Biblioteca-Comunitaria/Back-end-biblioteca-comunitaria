package com.biblioteca.ProjetoBiblioteca.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.biblioteca.ProjetoBiblioteca.entities.Bibliotecario;
import com.biblioteca.ProjetoBiblioteca.repositories.BibliotecarioRepository;
import com.biblioteca.ProjetoBiblioteca.service.BibliotecarioService;

/**
 * Como o cadastro público existe, este inicializador serve apenas para deixar a aplicação pronta no
 * primeiro uso: se a tabela estiver vazia, cria o bibliotecário inicial com o usuário e a senha da
 * configuração. Com a tabela já preenchida, não faz nada.
 */
@Component
public class BibliotecarioInicial implements ApplicationRunner {

	private final BibliotecarioRepository bibliotecarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final String usuario;
	private final String senha;
	private final String email;

	public BibliotecarioInicial(BibliotecarioRepository bibliotecarioRepository, PasswordEncoder passwordEncoder,
			@Value("${biblioteca.bibliotecario-inicial.usuario}") String usuario,
			@Value("${biblioteca.bibliotecario-inicial.senha}") String senha,
			@Value("${biblioteca.bibliotecario-inicial.email:}") String email) {
		this.bibliotecarioRepository = bibliotecarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.usuario = usuario;
		this.senha = senha;
		this.email = email;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (bibliotecarioRepository.count() > 0) {
			return;
		}
		if (senha == null || senha.isBlank()) {
			throw new IllegalStateException(
					"Não há nenhum bibliotecário cadastrado. Defina a variável BIBLIOTECA_SENHA para criar o bibliotecário inicial.");
		}
		bibliotecarioRepository.save(new Bibliotecario("Bibliotecário inicial",
				BibliotecarioService.normalizarUsuario(usuario), BibliotecarioService.normalizarEmail(email),
				passwordEncoder.encode(senha)));
	}
}
