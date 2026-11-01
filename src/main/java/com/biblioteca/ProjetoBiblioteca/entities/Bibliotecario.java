package com.biblioteca.ProjetoBiblioteca.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bibliotecarios")
public class Bibliotecario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;

	/** Nome de login, único e sempre em minúsculas. */
	@Column(nullable = false, unique = true)
	private String usuario;

	/** E-mail usado na recuperação de senha. */
	@Column(unique = true)
	private String email;

	/** Hash BCrypt da senha; a senha em texto nunca é gravada. */
	@Column(nullable = false)
	private String senha;

	/** Código temporário de recuperação de senha (6 dígitos). */
	@Column(length = 6)
	private String codigoRedefinicao;

	@Column
	private LocalDateTime codigoRedefinicaoExpiraEm;

	protected Bibliotecario() {
	}

	public Bibliotecario(String nome, String usuario, String senhaCriptografada) {
		this(nome, usuario, null, senhaCriptografada);
	}

	public Bibliotecario(String nome, String usuario, String email, String senhaCriptografada) {
		this.nome = nome;
		this.usuario = usuario;
		this.email = email;
		this.senha = senhaCriptografada;
	}

	public void iniciarRedefinicaoSenha(String codigo, LocalDateTime expiraEm) {
		this.codigoRedefinicao = codigo;
		this.codigoRedefinicaoExpiraEm = expiraEm;
	}

	public boolean codigoRedefinicaoValido(String codigo) {
		return this.codigoRedefinicao != null
				&& this.codigoRedefinicao.equals(codigo)
				&& this.codigoRedefinicaoExpiraEm != null
				&& LocalDateTime.now().isBefore(this.codigoRedefinicaoExpiraEm);
	}

	public void alterarSenha(String senhaCriptografada) {
		this.senha = senhaCriptografada;
		this.codigoRedefinicao = null;
		this.codigoRedefinicaoExpiraEm = null;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}
}
