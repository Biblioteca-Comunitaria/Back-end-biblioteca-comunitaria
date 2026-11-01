package com.biblioteca.ProjetoBiblioteca.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.biblioteca.ProjetoBiblioteca.entities.Usuario;
import com.biblioteca.ProjetoBiblioteca.repositories.UsuarioRepository;

@Service
public class AutenticacaoService {

	@Autowired
	private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Usuario> loginLeitor(Long cpf, String senha) {
        Optional<Usuario> usuario = usuarioService.buscarUsuarioPorCpf(cpf);
        if (usuario.isPresent() && passwordEncoder.matches(senha, usuario.get().getSenha())) {
            return usuario;
        }
        return Optional.empty();
    }

}
