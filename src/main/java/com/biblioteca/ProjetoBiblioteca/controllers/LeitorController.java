package com.biblioteca.ProjetoBiblioteca.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.ProjetoBiblioteca.dto.LeitorResponse;
import com.biblioteca.ProjetoBiblioteca.repositories.LeitorRepository;

/** Somente leitura: os leitores vêm do script data.sql e servem para identificar quem empresta. */
@RestController
@RequestMapping("/leitores")
public class LeitorController {

	private final LeitorRepository leitorRepository;

	public LeitorController(LeitorRepository leitorRepository) {
		this.leitorRepository = leitorRepository;
	}

	@GetMapping
	public List<LeitorResponse> listar() {
		return leitorRepository.findAll().stream().map(l -> new LeitorResponse(l.getId(), l.getNome())).toList();
	}
}
