package com.biblioteca.ProjetoBiblioteca.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.biblioteca.ProjetoBiblioteca.entities.Leitor;
import com.biblioteca.ProjetoBiblioteca.entities.Usuario;
import com.biblioteca.ProjetoBiblioteca.repositories.UsuarioRepository;
@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
	private PasswordEncoder passwordEncoder; 
    
    private final Path rootLocation = Paths.get("uploads");

    public <T extends Usuario> T salvarUsuario(T usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioRepository.save(usuario);
    }
    
    public void adicionarFoto(Long usuarioCpf, MultipartFile foto) throws IOException {
        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation); 
        }

        if (foto != null && !foto.isEmpty()) {
            String filename = usuarioCpf + "_" + foto.getOriginalFilename();
            Path destinationFile = this.rootLocation.resolve(filename).normalize().toAbsolutePath();

            Files.createDirectories(destinationFile.getParent());

            Files.copy(foto.getInputStream(), destinationFile);

            Usuario usuario = usuarioRepository.findById(usuarioCpf)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            usuario.setFoto(destinationFile.toString());

            usuarioRepository.save(usuario);
        }
    }

    public <T extends Usuario> T atualizarUsuario(Long cpf, T usuario) {
        T usuarioExistente = (T) usuarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Valida CPF: Verifica se o CPF informado não é de outro usuário
        if (!cpf.equals(usuario.getCpf())) {
            Optional<Usuario> usuarioPorCpf = usuarioRepository.findByCpf(usuario.getCpf());
            if (usuarioPorCpf.isPresent()) {
                throw new RuntimeException("Já existe um usuário com o CPF informado.");
            }
        }

        // Valida Email: Verifica se o email informado não é de outro usuário
        if (!usuario.getEmail().equals(usuarioExistente.getEmail())) {
            Optional<Usuario> usuarioPorEmail = usuarioRepository.findByEmail(usuario.getEmail());
            if (usuarioPorEmail.isPresent()) {
                throw new RuntimeException("Já existe um usuário com o email informado.");
            }
        }

        // Atualiza os dados do usuário existente
        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setEndereco(usuario.getEndereco());
        usuarioExistente.setTell(usuario.getTell());
        usuarioExistente.setFoto(usuario.getFoto());

        // Criptografa a senha se houver alteração
        if (usuario.getSenha() != null && !usuario.getSenha().equals(usuarioExistente.getSenha())) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        // Salva e retorna o usuário atualizado
        return usuarioRepository.save(usuarioExistente);
    }

    public Optional<Usuario> buscarUsuarioPorCpf(Long cpf) {
        return usuarioRepository.findByCpf(cpf);
    }

    public List<Usuario> buscarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public void deletarUsuario(Long cpf) {
        // Busca o usuário pelo CPF
        Usuario usuario = usuarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Se não for um Leitor, pode excluir diretamente
            usuarioRepository.delete(usuario);
 
    }

    // Valida a senha fornecida pelo usuário comparando com a armazenada
    public boolean validarSenha(String senhaFornecida, String senhaArmazenada) {
        return passwordEncoder.matches(senhaFornecida, senhaArmazenada);
    }
}