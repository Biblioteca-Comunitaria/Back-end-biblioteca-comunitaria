package com.biblioteca.ProjetoBiblioteca.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.ProjetoBiblioteca.dto.EmprestimoRequest;
import com.biblioteca.ProjetoBiblioteca.dto.EmprestimoResponse;
import com.biblioteca.ProjetoBiblioteca.service.EmprestimoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

	private final EmprestimoService emprestimoService;

	public EmprestimoController(EmprestimoService emprestimoService) {
		this.emprestimoService = emprestimoService;
	}

	/** RF3 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EmprestimoResponse emprestar(@Valid @RequestBody EmprestimoRequest request) {
		return emprestimoService.emprestar(request.livroId(), request.leitorId());
	}

	/** RF4 */
	@PutMapping("/{id}/devolucao")
	public EmprestimoResponse devolver(@PathVariable Long id) {
		return emprestimoService.devolver(id);
	}

	/** Lista todos os empréstimos (tela de empréstimos do front). */
	@GetMapping
	public List<EmprestimoResponse> listar() {
		return emprestimoService.listarTodos();
	}
}
