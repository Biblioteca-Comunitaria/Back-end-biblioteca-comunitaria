package com.biblioteca.ProjetoBiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.biblioteca.ProjetoBiblioteca.entities.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long> {

	boolean existsByIsbn(String isbn);

	@Query("select l from Livro l "
			+ "where lower(l.titulo) like lower(concat('%', :termo, '%')) "
			+ "or lower(l.autor) like lower(concat('%', :termo, '%')) "
			+ "order by l.titulo")
	List<Livro> buscarPorTituloOuAutor(@Param("termo") String termo);
}
