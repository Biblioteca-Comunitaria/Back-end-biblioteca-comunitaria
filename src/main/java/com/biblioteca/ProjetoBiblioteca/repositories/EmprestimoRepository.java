package com.biblioteca.ProjetoBiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.ProjetoBiblioteca.entities.Emprestimo;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

	/** Lista usada pela tela de empréstimos do front (mais recentes primeiro). */
	List<Emprestimo> findAllByOrderByDataEmprestimoDescIdDesc();
}
