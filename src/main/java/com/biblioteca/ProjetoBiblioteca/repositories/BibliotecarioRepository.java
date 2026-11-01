package com.biblioteca.ProjetoBiblioteca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.ProjetoBiblioteca.entities.Bibliotecario;

public interface BibliotecarioRepository extends JpaRepository<Bibliotecario, Long> {

	Optional<Bibliotecario> findByUsuario(String usuario);

	boolean existsByUsuario(String usuario);

	Optional<Bibliotecario> findByEmailIgnoreCase(String email);

	boolean existsByEmailIgnoreCase(String email);
}
