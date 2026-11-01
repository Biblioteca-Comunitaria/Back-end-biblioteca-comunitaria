package com.biblioteca.ProjetoBiblioteca.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "livros")
public class Livro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String titulo;

	@Column(nullable = false)
	private String autor;

	@Column(nullable = false, unique = true)
	private String isbn;

	@Column(nullable = false)
	private int quantidadeExemplares;

	@Column
	private String genero;

	@Column
	private String editora;

	@Column
	private Integer anoPublicacao;

	protected Livro() {
	}

	public Livro(String titulo, String autor, String isbn, int quantidadeExemplares) {
		this(titulo, autor, isbn, quantidadeExemplares, null, null, null);
	}

	public Livro(String titulo, String autor, String isbn, int quantidadeExemplares, String genero, String editora,
			Integer anoPublicacao) {
		this.titulo = titulo;
		this.autor = autor;
		this.isbn = isbn;
		this.quantidadeExemplares = quantidadeExemplares;
		this.genero = genero;
		this.editora = editora;
		this.anoPublicacao = anoPublicacao;
	}

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getAutor() {
		return autor;
	}

	public String getIsbn() {
		return isbn;
	}

	public int getQuantidadeExemplares() {
		return quantidadeExemplares;
	}

	public String getGenero() {
		return genero;
	}

	public String getEditora() {
		return editora;
	}

	public Integer getAnoPublicacao() {
		return anoPublicacao;
	}
}
