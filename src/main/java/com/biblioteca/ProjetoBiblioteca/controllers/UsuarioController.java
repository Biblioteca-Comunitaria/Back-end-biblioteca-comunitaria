package com.biblioteca.ProjetoBiblioteca.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.biblioteca.ProjetoBiblioteca.entities.Usuario;
import com.biblioteca.ProjetoBiblioteca.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/listar")
	public List<Usuario> buscarTodos() {
		return usuarioService.buscarTodosUsuarios();
	}

	@GetMapping("/listar/{cpf}")
	public ResponseEntity<Usuario> buscarPorCpf(@PathVariable Long cpf) {
		return usuarioService.buscarUsuarioPorCpf(cpf).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/deletar/{cpf}")
	public ResponseEntity<Void> deletar(@PathVariable Long cpf) {
		if (usuarioService.buscarUsuarioPorCpf(cpf).isPresent()) {
			usuarioService.deletarUsuario(cpf);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/adicionarFoto/{cpf}")
    public ResponseEntity<String> adicionarFoto(@PathVariable Long cpf, @RequestParam("foto") MultipartFile foto) {
        try {
            usuarioService.adicionarFoto(cpf, foto); 
            return ResponseEntity.ok("Foto adicionada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao adicionar a foto: " + e.getMessage());
        }
    }
	
    @GetMapping("/verFoto/{cpf}")
    public ResponseEntity<Resource> getFoto(@PathVariable Long cpf) throws IOException {
        Usuario usuario = usuarioService.buscarUsuarioPorCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Path fotoPath = Paths.get(usuario.getFoto());
        
        Resource resource = new FileSystemResource(fotoPath);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) 
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fotoPath.getFileName() + "\"")
                .body(resource);
    }
}
