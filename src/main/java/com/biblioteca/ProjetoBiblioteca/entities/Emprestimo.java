package com.biblioteca.ProjetoBiblioteca.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "emprestimos")
public class Emprestimo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "livro_id", nullable = false)
	private Livro livro;

	@ManyToOne(optional = false)
	@JoinColumn(name = "leitor_id", nullable = false)
	private Leitor leitor;

	@Column(nullable = false)
	private LocalDate dataEmprestimo;

	/** Data prevista para devolução: dataEmprestimo + 14 dias. */
	@Column(nullable = false)
	private LocalDate dataPrevistaDevolucao;

	/** Data em que o livro foi realmente devolvido; nula enquanto o empréstimo está ativo. */
	private LocalDate dataDevolucao;

	protected Emprestimo() {
	}

	public Emprestimo(Livro livro, Leitor leitor, LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao) {
		this.livro = livro;
		this.leitor = leitor;
		this.dataEmprestimo = dataEmprestimo;
		this.dataPrevistaDevolucao = dataPrevistaDevolucao;
	}

	public boolean isDevolvido() {
		return dataDevolucao != null;
	}

	public void registrarDevolucao(LocalDate data) {
		this.dataDevolucao = data;
	}

	public Long getId() {
		return id;
	}

	public Livro getLivro() {
		return livro;
	}

	public Leitor getLeitor() {
		return leitor;
	}

	public LocalDate getDataEmprestimo() {
		return dataEmprestimo;
	}

	public LocalDate getDataPrevistaDevolucao() {
		return dataPrevistaDevolucao;
	}

	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}
}
