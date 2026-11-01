package com.biblioteca.ProjetoBiblioteca.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.biblioteca.ProjetoBiblioteca.dto.EmprestimoResponse;
import com.biblioteca.ProjetoBiblioteca.entities.Emprestimo;
import com.biblioteca.ProjetoBiblioteca.entities.Leitor;
import com.biblioteca.ProjetoBiblioteca.entities.Livro;
import com.biblioteca.ProjetoBiblioteca.repositories.EmprestimoRepository;
import com.biblioteca.ProjetoBiblioteca.repositories.LeitorRepository;
import com.biblioteca.ProjetoBiblioteca.repositories.LivroRepository;

@Service
public class EmprestimoService {

	static final int PRAZO_EM_DIAS = 14;

	private final EmprestimoRepository emprestimoRepository;
	private final LivroRepository livroRepository;
	private final LeitorRepository leitorRepository;

	public EmprestimoService(EmprestimoRepository emprestimoRepository, LivroRepository livroRepository,
			LeitorRepository leitorRepository) {
		this.emprestimoRepository = emprestimoRepository;
		this.livroRepository = livroRepository;
		this.leitorRepository = leitorRepository;
	}

	/**
	 * RF3: registra o empréstimo. A devolução prevista é a data do empréstimo + 14 dias.
	 */
	@Transactional
	public EmprestimoResponse emprestar(Long livroId, Long leitorId) {
		Livro livro = livroRepository.findById(livroId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado."));
		Leitor leitor = leitorRepository.findById(leitorId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leitor não encontrado."));

		LocalDate hoje = LocalDate.now();
		Emprestimo emprestimo = emprestimoRepository
				.save(new Emprestimo(livro, leitor, hoje, hoje.plusDays(PRAZO_EM_DIAS)));
		return EmprestimoResponse.de(emprestimo);
	}

	/** RF4: registra a data da devolução. */
	@Transactional
	public EmprestimoResponse devolver(Long emprestimoId) {
		Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado."));
		if (emprestimo.isDevolvido()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Este empréstimo já foi devolvido em " + emprestimo.getDataDevolucao() + ".");
		}
		emprestimo.registrarDevolucao(LocalDate.now());
		return EmprestimoResponse.de(emprestimoRepository.save(emprestimo));
	}

	/** Todos os empréstimos, do mais recente para o mais antigo (tela de empréstimos do front). */
	@Transactional(readOnly = true)
	public List<EmprestimoResponse> listarTodos() {
		return emprestimoRepository.findAllByOrderByDataEmprestimoDescIdDesc().stream()
				.map(EmprestimoResponse::de).toList();
	}
}
