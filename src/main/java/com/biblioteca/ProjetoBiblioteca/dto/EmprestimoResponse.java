package com.biblioteca.ProjetoBiblioteca.dto;

import java.time.LocalDate;

import com.biblioteca.ProjetoBiblioteca.entities.Emprestimo;

public record EmprestimoResponse(
		Long id,
		Long livroId,
		String tituloLivro,
		Long leitorId,
		String nomeLeitor,
		LocalDate dataEmprestimo,
		LocalDate dataPrevistaDevolucao,
		LocalDate dataDevolucao) {

	public static EmprestimoResponse de(Emprestimo e) {
		return new EmprestimoResponse(
				e.getId(),
				e.getLivro().getId(),
				e.getLivro().getTitulo(),
				e.getLeitor().getId(),
				e.getLeitor().getNome(),
				e.getDataEmprestimo(),
				e.getDataPrevistaDevolucao(),
				e.getDataDevolucao());
	}
}
