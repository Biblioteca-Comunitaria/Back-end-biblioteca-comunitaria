package com.biblioteca.ProjetoBiblioteca.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.ProjetoBiblioteca.dto.CadastroLivroRequest;
import com.biblioteca.ProjetoBiblioteca.dto.LivroResponse;
import com.biblioteca.ProjetoBiblioteca.service.LivroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/livros")
public class LivroController {

	private final LivroService livroService;

	public LivroController(LivroService livroService) {
		this.livroService = livroService;
	}

	/** RF1 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LivroResponse cadastrar(@Valid @RequestBody CadastroLivroRequest request) {
		return livroService.cadastrar(request);
	}

	/** RF2: GET /livros?q=termo (sem q, lista todos) */
	@GetMapping
	public List<LivroResponse> buscar(@RequestParam(name = "q", required = false) String q) {
		return livroService.buscar(q);
	}
}
