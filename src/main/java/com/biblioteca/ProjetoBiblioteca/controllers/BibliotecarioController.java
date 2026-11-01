package com.biblioteca.ProjetoBiblioteca.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.ProjetoBiblioteca.dto.BibliotecarioResponse;
import com.biblioteca.ProjetoBiblioteca.dto.CadastroBibliotecarioRequest;
import com.biblioteca.ProjetoBiblioteca.service.BibliotecarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bibliotecarios")
public class BibliotecarioController {

	private final BibliotecarioService bibliotecarioService;

	public BibliotecarioController(BibliotecarioService bibliotecarioService) {
		this.bibliotecarioService = bibliotecarioService;
	}

	/** Cadastra um novo bibliotecário. Só quem já está logado pode cadastrar outro. */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BibliotecarioResponse cadastrar(@Valid @RequestBody CadastroBibliotecarioRequest request) {
		return bibliotecarioService.cadastrar(request);
	}
}
