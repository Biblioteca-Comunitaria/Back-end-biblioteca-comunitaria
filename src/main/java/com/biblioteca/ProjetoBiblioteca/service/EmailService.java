package com.biblioteca.ProjetoBiblioteca.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final JavaMailSender mailSender;
	private final String remetente;

	public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username:}") String remetente) {
		this.mailSender = mailSender;
		this.remetente = remetente;
	}

	public void enviarEmail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(remetente);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);

		try {
			mailSender.send(message);
			System.out.println("E-mail enviado com sucesso para " + to);
		} catch (Exception e) {
			System.err.println("Erro ao enviar o e-mail: " + e.getMessage());
		}
	}
}
