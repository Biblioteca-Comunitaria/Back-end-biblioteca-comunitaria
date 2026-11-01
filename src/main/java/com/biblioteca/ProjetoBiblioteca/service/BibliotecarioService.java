package com.biblioteca.ProjetoBiblioteca.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.biblioteca.ProjetoBiblioteca.dto.BibliotecarioResponse;
import com.biblioteca.ProjetoBiblioteca.dto.CadastroBibliotecarioRequest;
import com.biblioteca.ProjetoBiblioteca.entities.Bibliotecario;
import com.biblioteca.ProjetoBiblioteca.repositories.BibliotecarioRepository;

@Service
public class BibliotecarioService {

	private static final SecureRandom SORTEIO = new SecureRandom();
	private static final int MINUTOS_VALIDADE_CODIGO = 15;

	private final BibliotecarioRepository bibliotecarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	public BibliotecarioService(BibliotecarioRepository bibliotecarioRepository, PasswordEncoder passwordEncoder,
			EmailService emailService) {
		this.bibliotecarioRepository = bibliotecarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
	}

	/** O login não diferencia maiúsculas de minúsculas: "Maria" e "maria" são o mesmo usuário. */
	public static String normalizarUsuario(String usuario) {
		return usuario.trim().toLowerCase(Locale.ROOT);
	}

	public static String normalizarEmail(String email) {
		if (email == null || email.isBlank()) {
			return null;
		}
		return email.trim().toLowerCase(Locale.ROOT);
	}

	@Transactional
	public BibliotecarioResponse cadastrar(CadastroBibliotecarioRequest request) {
		String usuario = normalizarUsuario(request.usuario());
		String email = normalizarEmail(request.email());

		if (bibliotecarioRepository.existsByUsuario(usuario)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Já existe um bibliotecário com o usuário " + usuario + ".");
		}
		if (email != null && bibliotecarioRepository.existsByEmailIgnoreCase(email)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um bibliotecário com esse e-mail.");
		}

		Bibliotecario bibliotecario = bibliotecarioRepository.save(new Bibliotecario(request.nome().trim(), usuario,
				email, passwordEncoder.encode(request.senha())));
		return BibliotecarioResponse.de(bibliotecario);
	}

	@Transactional(readOnly = true)
	public BibliotecarioResponse buscarPorUsuario(String usuario) {
		return bibliotecarioRepository.findByUsuario(normalizarUsuario(usuario))
				.map(BibliotecarioResponse::de)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bibliotecário não encontrado."));
	}

	/**
	 * Gera um código de 6 dígitos e envia para o e-mail cadastrado. A resposta é sempre a mesma para não
	 * revelar se o e-mail existe ou não.
	 */
	@Transactional
	public void solicitarRedefinicaoSenha(String emailInformado) {
		String email = normalizarEmail(emailInformado);
		if (email == null) {
			return;
		}

		Optional<Bibliotecario> encontrado = bibliotecarioRepository.findByEmailIgnoreCase(email);
		if (encontrado.isEmpty()) {
			return;
		}

		Bibliotecario bibliotecario = encontrado.get();
		String codigo = String.format("%06d", SORTEIO.nextInt(1_000_000));
		bibliotecario.iniciarRedefinicaoSenha(codigo, LocalDateTime.now().plusMinutes(MINUTOS_VALIDADE_CODIGO));
		bibliotecarioRepository.save(bibliotecario);

		String mensagem = "Olá, " + bibliotecario.getNome() + "!\n\n"
				+ "Seu código para redefinir a senha da Biblioteca Luiz Brasil é: " + codigo + "\n\n"
				+ "Ele expira em " + MINUTOS_VALIDADE_CODIGO + " minutos. Se você não pediu a alteração, ignore este e-mail.";
		emailService.enviarEmail(bibliotecario.getEmail(), "Recuperação de senha - Biblioteca Luiz Brasil", mensagem);

		// Ajuda durante o desenvolvimento local quando o SMTP ainda não está configurado.
		System.out.println("Código de recuperação de senha para " + bibliotecario.getEmail() + ": " + codigo);
	}

	@Transactional
	public void redefinirSenha(String emailInformado, String codigo, String novaSenha) {
		String email = normalizarEmail(emailInformado);
		Bibliotecario bibliotecario = (email == null ? Optional.<Bibliotecario>empty()
				: bibliotecarioRepository.findByEmailIgnoreCase(email))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Código inválido ou expirado. Solicite um novo código."));

		if (!bibliotecario.codigoRedefinicaoValido(codigo == null ? "" : codigo.trim())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Código inválido ou expirado. Solicite um novo código.");
		}

		bibliotecario.alterarSenha(passwordEncoder.encode(novaSenha));
		bibliotecarioRepository.save(bibliotecario);
	}
}
