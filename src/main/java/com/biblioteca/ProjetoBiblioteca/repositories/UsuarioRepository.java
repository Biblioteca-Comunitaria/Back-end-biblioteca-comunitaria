package com.biblioteca.ProjetoBiblioteca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.ProjetoBiblioteca.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Optional<Usuario> findByCpf(Long cpf);

	void deleteByCpf(Long cpf);

	Optional<Usuario> findByEmail(String email);
	
}
