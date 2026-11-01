package com.biblioteca.ProjetoBiblioteca.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class ConsultaLivroService {
    private final String URL_BASE = "https://www.googleapis.com/books/v1/volumes?q=isbn:"; // Exemplo de API para consulta

    public Map<String, Object> buscarDadosLivro(String isbn) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = URL_BASE + isbn;
            Map<String, Object> resposta = restTemplate.getForObject(url, Map.class);
            List<Map<String, Object>> items = (List<Map<String, Object>>) resposta.get("items");
            if (items != null && !items.isEmpty()) {
                return items.get(0); 
            }
            return null; 
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }
}
