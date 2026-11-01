package com.biblioteca.ProjetoBiblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.ProjetoBiblioteca.entities.Leitor;

public interface LeitorRepository extends JpaRepository<Leitor, Long> {
}
