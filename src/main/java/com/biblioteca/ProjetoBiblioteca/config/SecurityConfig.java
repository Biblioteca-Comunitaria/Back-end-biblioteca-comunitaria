package com.biblioteca.ProjetoBiblioteca.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Protege a API: as rotas de autenticação pública (cadastro, esqueci a senha e redefinir senha) não
 * exigem login; as demais exigem HTTP Basic de um bibliotecário cadastrado no banco (ver
 * BibliotecarioUserDetailsService). Sem credenciais ou com senha errada a resposta é 401.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final List<String> origensPermitidas;

	public SecurityConfig(@Value("${biblioteca.cors.origins:http://localhost:5173,http://127.0.0.1:5173,http://localhost:4173,http://127.0.0.1:4173}") String origens) {
		this.origensPermitidas = Arrays.stream(origens.split(",")).map(String::trim).filter(s -> !s.isBlank())
				.toList();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// API sem sessão e sem cookies (a credencial vai em todo request), então não há risco de CSRF
				.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults())
				.sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(regras -> regras
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/error").permitAll()
						.requestMatchers(HttpMethod.POST, "/auth/cadastro", "/auth/esqueci-senha", "/auth/redefinir-senha")
						.permitAll()
						.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuracao = new CorsConfiguration();
		configuracao.setAllowedOrigins(origensPermitidas);
		configuracao.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuracao.setAllowedHeaders(List.of("*"));
		configuracao.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource origem = new UrlBasedCorsConfigurationSource();
		origem.registerCorsConfiguration("/**", configuracao);
		return origem;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
