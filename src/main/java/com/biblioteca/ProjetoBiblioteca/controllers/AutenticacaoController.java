package com.biblioteca.ProjetoBiblioteca.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.ProjetoBiblioteca.dto.BibliotecarioResponse;
import com.biblioteca.ProjetoBiblioteca.dto.CadastroBibliotecarioRequest;
import com.biblioteca.ProjetoBiblioteca.dto.EsqueciSenhaRequest;
import com.biblioteca.ProjetoBiblioteca.dto.MensagemResponse;
import com.biblioteca.ProjetoBiblioteca.dto.RedefinirSenhaRequest;
import com.biblioteca.ProjetoBiblioteca.service.BibliotecarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

	private final BibliotecarioService bibliotecarioService;

	public AutenticacaoController(BibliotecarioService bibliotecarioService) {
		this.bibliotecarioService = bibliotecarioService;
	}

	/**
	 * Login: o Spring Security valida o usuário e a senha enviados no cabeçalho Basic (401 se
	 * estiverem errados) e esta rota devolve os dados de quem entrou.
	 */
	@PostMapping("/login")
	public BibliotecarioResponse login(Authentication authentication) {
		return bibliotecarioService.buscarPorUsuario(authentication.getName());
	}

	/** Cadastro aberto, usado pela tela de cadastro do front. */
	@PostMapping("/cadastro")
	@ResponseStatus(HttpStatus.CREATED)
	public BibliotecarioResponse cadastrar(@Valid @RequestBody CadastroBibliotecarioRequest request) {
		return bibliotecarioService.cadastrar(request);
	}

	/** Envia um código de 6 dígitos para o e-mail cadastrado. */
	@PostMapping("/esqueci-senha")
	public MensagemResponse esqueciSenha(@Valid @RequestBody EsqueciSenhaRequest request) {
		bibliotecarioService.solicitarRedefinicaoSenha(request.email());
		return new MensagemResponse("Se o e-mail estiver cadastrado, enviaremos um código de recuperação.");
	}

	/** Troca a senha usando o código enviado por e-mail. */
	@PostMapping("/redefinir-senha")
	public MensagemResponse redefinirSenha(@Valid @RequestBody RedefinirSenhaRequest request) {
		bibliotecarioService.redefinirSenha(request.email(), request.codigo(), request.novaSenha());
		return new MensagemResponse("Senha alterada com sucesso.");
	}
}
