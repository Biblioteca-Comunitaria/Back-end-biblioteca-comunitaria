package com.biblioteca.ProjetoBiblioteca.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.biblioteca.ProjetoBiblioteca.dto.CadastroLivroRequest;
import com.biblioteca.ProjetoBiblioteca.dto.LivroResponse;
import com.biblioteca.ProjetoBiblioteca.entities.Livro;
import com.biblioteca.ProjetoBiblioteca.repositories.LivroRepository;

@Service
public class LivroService {

	private final LivroRepository livroRepository;

	public LivroService(LivroRepository livroRepository) {
		this.livroRepository = livroRepository;
	}

	/** RF1: cadastra um livro com ISBN único e pelo menos 1 exemplar. */
	@Transactional
	public LivroResponse cadastrar(CadastroLivroRequest request) {
		String isbn = request.isbn().trim();
		if (livroRepository.existsByIsbn(isbn)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um livro cadastrado com o ISBN " + isbn + ".");
		}
		Livro livro = livroRepository.save(new Livro(request.titulo().trim(), request.autor().trim(), isbn,
				request.quantidadeExemplares(), textoOpcional(request.genero()), textoOpcional(request.editora()),
				request.anoPublicacao()));
		return paraResposta(livro);
	}

	/** RF2: busca por trecho do título ou do autor (sem diferenciar maiúsculas). */
	@Transactional(readOnly = true)
	public List<LivroResponse> buscar(String termo) {
		String filtro = termo == null ? "" : termo.trim();
		return livroRepository.buscarPorTituloOuAutor(filtro).stream().map(this::paraResposta).toList();
	}

	private LivroResponse paraResposta(Livro livro) {
		return new LivroResponse(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getIsbn(),
				livro.getQuantidadeExemplares(), livro.getGenero(), livro.getEditora(), livro.getAnoPublicacao());
	}

	private static String textoOpcional(String valor) {
		return valor == null || valor.isBlank() ? null : valor.trim();
	}
}
